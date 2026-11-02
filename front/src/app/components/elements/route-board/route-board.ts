import { Component, ElementRef, Input, OnChanges, OnDestroy, AfterViewInit, PLATFORM_ID, inject, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Trip } from '../../../models/trip.model';
import { Campus } from '../../../models/campus.model';

/** Una localidad o un campus colocado en el tablero */
interface BoardStop {
    name: string;
    label: string;
    y: number;
    active: boolean;
}

/** Ruta localidad-campus con al menos un viaje publicado */
interface BoardRoute {
    key: string;
    town: string;
    campus: string;
    d: string;
    time: string;
    toCampus: boolean;
    begin: string;
    dur: string;
    /** Coche del viaje, para el resumen accesible (p. ej. "Seat Leon blanco") */
    car: string;
    /** Id del filtro que pinta la carrocería del color del coche real; null deja el rojo del logo */
    paint: string | null;
}

/**
 * Pinturas de carrocería por color de coche. Cada una tiñe solo las zonas rojas del dibujo
 * (cristales, ruedas y faros se quedan igual): color de destino y ganancia sobre la luminosidad
 * del rojo original, para conservar brillos y sombras.
 */
const CAR_PAINTS: Record<string, { rgb: [number, number, number]; gain: number }> = {
    blanco: { rgb: [0.96, 0.96, 0.97], gain: 2.6 },
    gris: { rgb: [0.55, 0.57, 0.6], gain: 2.1 },
    plata: { rgb: [0.78, 0.8, 0.84], gain: 2.4 },
    'gris plata': { rgb: [0.78, 0.8, 0.84], gain: 2.4 },
    negro: { rgb: [0.22, 0.23, 0.26], gain: 1.4 },
    azul: { rgb: [0.2, 0.45, 0.95], gain: 2.3 },
    'azul oscuro': { rgb: [0.12, 0.2, 0.55], gain: 2.2 },
    verde: { rgb: [0.15, 0.7, 0.4], gain: 2.3 },
    amarillo: { rgb: [1, 0.83, 0.12], gain: 2.7 },
    naranja: { rgb: [1, 0.5, 0.1], gain: 2.5 },
    blanca: { rgb: [0.96, 0.96, 0.97], gain: 2.6 },
};

/** Normaliza un nombre de color ("Azul Oscuro", "gris  plata") para buscarlo en CAR_PAINTS */
function paintKey(color?: string): string | null {
    if (!color) return null;
    const key = color.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase().replace(/\s+/g, ' ').trim();
    return key in CAR_PAINTS ? key : null;
}

/**
 * Tablero de rutas del hero: las localidades de origen de los viajes publicados a la izquierda,
 * los campus a la derecha y, por cada ruta con viaje, el coche de UniCar recorriéndola con su
 * hora de salida. Todo sale de datos reales; con movimiento reducido el tablero queda estático.
 */
@Component({
    selector: 'app-route-board',
    standalone: true,
    templateUrl: './route-board.html',
    styleUrl: './route-board.css',
})
export class RouteBoard implements OnChanges, AfterViewInit, OnDestroy {
    @Input() trips: Trip[] = [];
    @Input() campuses: Campus[] = [];

    private host = inject(ElementRef<HTMLElement>);
    private platformId = inject(PLATFORM_ID);
    private cdr = inject(ChangeDetectorRef);
    private zone = inject(NgZone);
    private resizeObserver?: ResizeObserver;
    private measureCtx: CanvasRenderingContext2D | null = null;

    width = 0;
    height = 0;
    rowGap = 44;
    fontSize = 13;
    carSize = 30;
    townX = 0;
    campusX = 0;
    reducedMotion = false;

    towns: BoardStop[] = [];
    stops: BoardStop[] = [];
    routes: BoardRoute[] = [];
    /** Localidad o campus resaltado al pasar el ratón */
    focus: string | null = null;

    get summary(): string {
        if (!this.routes.length) return 'Campus de destino: ' + this.campuses.map(c => c.name).join(', ') + '.';
        return 'Rutas con viajes publicados: ' + this.routes
            .map(r => `${r.town} a ${r.campus} a las ${r.time}${r.car ? ' (' + r.car + ')' : ''}`).join('; ') + '.';
    }

    /** Filtros de pintura que usan los coches del tablero, uno por color distinto */
    get paints(): { id: string; recolor: string }[] {
        const used = [...new Set(this.routes.map(r => r.paint).filter((p): p is string => !!p))];
        return used.map(id => {
            const { rgb, gain } = CAR_PAINTS[id.slice('rb-paint-'.length).replace(/-/g, ' ')];
            // Cada canal de salida = color de destino × ganancia × luminosidad del píxel original
            const row = (c: number) => [0.3, 0.59, 0.11].map(w => (c * gain * w).toFixed(3)).join(' ') + ' 0 0';
            return { id, recolor: `${row(rgb[0])} ${row(rgb[1])} ${row(rgb[2])} 0 0 0 1 0` };
        });
    }

    ngAfterViewInit(): void {
        if (!isPlatformBrowser(this.platformId)) return;
        this.reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        this.measureCtx = document.createElement('canvas').getContext('2d');
        this.resizeObserver = new ResizeObserver(() => this.zone.run(() => this.layout()));
        this.resizeObserver.observe(this.host.nativeElement);
        this.layout();
    }

    ngOnChanges(): void {
        if (this.measureCtx) this.layout();
    }

    ngOnDestroy(): void {
        this.resizeObserver?.disconnect();
    }

    isOn(route: BoardRoute): boolean {
        return !this.focus || this.focus === route.town || this.focus === route.campus;
    }

    /** Recalcula posiciones con el ancho real, para que el texto mida píxeles de verdad */
    private layout(): void {
        const width = Math.round(this.host.nativeElement.getBoundingClientRect().width);
        if (!width) return;
        const narrow = width < 460;
        this.fontSize = narrow ? 12 : 13;
        this.rowGap = narrow ? 34 : 44;
        // En móvil el tramo central es estrecho: coche más pequeño y menos rutas a la vez
        this.carSize = narrow ? 24 : 30;
        const maxTowns = narrow ? 4 : 6;
        const maxRoutes = narrow ? 4 : 8;

        // Una ruta por pareja localidad-campus; se queda la primera salida.
        // Solo rutas cuya localidad está en el tablero.
        const byKey = new Map<string, Trip>();
        for (const t of this.trips) {
            if (!t.townDTO?.name || !t.campusDTO?.name) continue;
            const key = `${t.townDTO.name}|${t.campusDTO.name}`;
            if (!byKey.has(key)) byKey.set(key, t);
        }
        const townNames = [...new Set([...byKey.values()].map(t => t.townDTO.name))].slice(0, maxTowns);
        const tripList = [...byKey.values()].filter(t => townNames.includes(t.townDTO.name)).slice(0, maxRoutes);
        const activeCampus = new Set(tripList.map(t => t.campusDTO.name));

        const labelW = Math.floor(width * (narrow ? 0.35 : 0.34));
        this.townX = labelW + 10;
        this.campusX = width - labelW - 10;

        const rows = Math.max(this.campuses.length, townNames.length, 1);
        const top = 18;
        this.height = top * 2 + (rows - 1) * this.rowGap;
        this.width = width;

        const font = `${this.fontSize}px 'DM Sans', sans-serif`;
        this.stops = this.campuses.map((c, i) => ({
            name: c.name,
            label: this.fit(c.name, labelW - 4, font),
            y: top + i * this.rowGap,
            active: activeCampus.has(c.name),
        }));
        // Las localidades se centran en vertical frente a la columna de campus
        const townTop = top + ((rows - townNames.length) * this.rowGap) / 2;
        this.towns = townNames.map((n, i) => ({
            name: n,
            label: this.fit(n, labelW - 4, font),
            y: townTop + i * this.rowGap,
            active: true,
        }));

        const mid = (this.townX + this.campusX) / 2;
        // Todas las rutas tardan lo mismo y salen escalonadas a lo largo de ese tiempo,
        // para que los coches no coincidan en el tramo central
        const durSec = Math.max(5, Math.round((this.campusX - this.townX) / 45));
        this.routes = tripList.map((t, i) => {
            const a = this.towns.find(s => s.name === t.townDTO.name)!;
            const b = this.stops.find(s => s.name === t.campusDTO.name);
            const by = b ? b.y : a.y;
            return {
                key: `${t.townDTO.name}|${t.campusDTO.name}`,
                town: t.townDTO.name,
                campus: t.campusDTO.name,
                d: `M ${this.townX} ${a.y} C ${mid} ${a.y}, ${mid} ${by}, ${this.campusX} ${by}`,
                time: (t.departureTime || '').slice(0, 5),
                toCampus: t.isToCampus !== false,
                begin: `${((i * durSec) / tripList.length).toFixed(2)}s`,
                dur: `${durSec}s`,
                car: [t.carDTO?.model, t.carDTO?.color?.toLowerCase()].filter(Boolean).join(' '),
                paint: (k => k ? 'rb-paint-' + k.replace(/ /g, '-') : null)(paintKey(t.carDTO?.color)),
            };
        });
        this.cdr.detectChanges();
    }

    /** Recorta un nombre largo con "…" para que quepa en su columna (el nombre completo va en el title) */
    private fit(text: string, maxWidth: number, font: string): string {
        const ctx = this.measureCtx;
        if (!ctx) return text;
        ctx.font = font;
        if (ctx.measureText(text).width <= maxWidth) return text;
        let cut = text;
        while (cut.length > 1 && ctx.measureText(cut + '…').width > maxWidth) cut = cut.slice(0, -1);
        return cut.trimEnd() + '…';
    }
}
