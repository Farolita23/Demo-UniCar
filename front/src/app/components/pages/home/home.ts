/**
 * Componente de Página de Inicio (Home)
 * 
 * Página principal de la aplicación que muestra:
 * - Sección hero con llamada a la acción
 * - Pasos de cómo usar la plataforma
 * - Viajes sugeridos (personalizados o generales)
 * - Estadísticas de la plataforma
 * - Sección de call-to-action final
 */

// Importaciones de Angular core
import { Component, inject, OnInit, AfterViewInit, OnDestroy, ChangeDetectorRef, PLATFORM_ID, ViewChild, ElementRef } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';

// Importaciones de servicios
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';

// Importaciones de modelos
import { Trip } from '../../../models/trip.model';

//Importacion iconos
import { AppIcon, IconName } from '../../elements/icon/icon';

/** Un nodo de la red interactiva del hero (representa a alguien buscando trayecto) */
interface HeroNode {
  x: number;
  y: number;
  vx: number;
  vy: number;
  r: number;
  color: string;
  /** Brillo del nodo (0 a 1), como la magnitud de una estrella: no cambia el tono, solo cuánto irradia */
  brightness: number;
  /** Opacidad actual por el ciclo de aparición/desaparición (0 a 1) */
  alpha: number;
  /** Fase del ciclo: visible un tiempo, se apaga, reaparece en otro punto, se enciende */
  fade: 'in' | 'visible' | 'out';
  /** Fotogramas restantes en la fase actual */
  timer: number;
}

/**
 * Componente Home
 * 
 * Página de inicio que muestra información sobre la plataforma,
 * características principales y viajes sugeridos personalizados o generales.
 */
@Component({
  // Selector CSS para usar el componente en templates
  selector: 'page-home',
  // Componente standalone sin necesidad de módulo
  standalone: true,
  // Módulos y componentes importados
  imports: [RouterLink, CommonModule, Header, Footer, TripCard, AppIcon],
  // Archivo HTML de la plantilla
  templateUrl: './home.html',
  // Archivo CSS de estilos
  styleUrl: './home.css',
})
export class Home implements OnInit, AfterViewInit, OnDestroy {
  // Inyección de servicios
  api        = inject(ApiService);
  auth       = inject(AuthService);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  /** Lista de viajes sugeridos a mostrar en la página */
  suggestedTrips: Trip[] = [];
  
  /** Indicador de carga de viajes sugeridos */
  loadingTrips = true;
  
  /** Indica si los viajes son personalizados (true) o generales (false) */
  isSuggested  = false;

  /** Referencia al lienzo de la red interactiva del hero */
  @ViewChild('heroCanvas') heroCanvasRef?: ElementRef<HTMLCanvasElement>;

  private ctx: CanvasRenderingContext2D | null = null;
  private nodes: HeroNode[] = [];
  private nodeColors: string[] = [];
  private mouse = { x: 0, y: 0, active: false };
  private rafId = 0;
  private reducedMotion = false;
  private resizeObserver?: ResizeObserver;
  private onResize = () => this.resizeHeroCanvas();
  /** Duración en fotogramas de la transición de apagado/encendido de cada nodo (~0.75s a 60fps) */
  private readonly fadeFrames = 45;

  /**
   * Hook de Angular: el canvas ya existe en el DOM, se puede inicializar la red
   */
  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId) || !this.heroCanvasRef) return;
    this.setupHeroNetwork(this.heroCanvasRef.nativeElement);
  }

  /**
   * Hook de Angular: limpieza al destruir el componente para no dejar
   * el bucle de animación ni los listeners corriendo en segundo plano
   */
  ngOnDestroy(): void {
    cancelAnimationFrame(this.rafId);
    this.resizeObserver?.disconnect();
    if (isPlatformBrowser(this.platformId)) {
      window.removeEventListener('resize', this.onResize);
    }
  }

  /**
   * Prepara la red: colores desde las variables CSS de la marca, tamaño del lienzo,
   * nodos iniciales y arranque del bucle de animación.
   */
  private setupHeroNetwork(canvas: HTMLCanvasElement): void {
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
    this.ctx = ctx;
    this.reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    // Toma los colores reales de la app: solo la gama de rojos, nada de acento ni éxito
    const styles = getComputedStyle(canvas);
    this.nodeColors = [
      styles.getPropertyValue('--primary').trim() || '#e63946',
      styles.getPropertyValue('--primary-light').trim() || '#ff6b73',
    ];

    this.resizeHeroCanvas();
    window.addEventListener('resize', this.onResize);
    this.resizeObserver = new ResizeObserver(this.onResize);
    this.resizeObserver.observe(canvas.parentElement ?? canvas);

    this.animateHeroNetwork(canvas);
  }

  /** Ajusta el tamaño del canvas a su contenedor y regenera los nodos */
  private resizeHeroCanvas(): void {
    const canvas = this.heroCanvasRef?.nativeElement;
    if (!canvas || !this.ctx) return;
    const rect = (canvas.parentElement ?? canvas).getBoundingClientRect();
    const dpr = window.devicePixelRatio || 1;
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    canvas.style.width = `${rect.width}px`;
    canvas.style.height = `${rect.height}px`;
    this.ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    this.createNodes(rect.width, rect.height);
  }

  /** Genera los nodos de la red, con densidad proporcional al tamaño del hero */
  private createNodes(width: number, height: number): void {
    const count = Math.min(70, Math.max(28, Math.round((width * height) / 15000)));
    this.nodes = Array.from({ length: count }, (): HeroNode => {
      const brightness = Math.random();
      return {
        x: Math.random() * width,
        y: Math.random() * height,
        vx: (Math.random() - 0.5) * 0.28,
        vy: (Math.random() - 0.5) * 0.28,
        r: Math.random() * 1.6 + 1.3,
        // Nodos más brillantes tienden al rojo claro, los apagados al rojo oscuro; sigue siendo la misma gama
        color: this.nodeColors[brightness > 0.5 ? 1 : 0],
        brightness,
        alpha: 1,
        fade: 'visible',
        // Arranque escalonado: cada nodo empieza en un punto distinto de su ciclo de vida,
        // así no aparecen ni desaparecen todos a la vez
        timer: Math.round(Math.random() * this.visibleFrames()),
      };
    });
  }

  /** Duración aleatoria (en fotogramas) que un nodo permanece visible antes de apagarse y reaparecer */
  private visibleFrames(): number {
    return 260 + Math.random() * 380;
  }

  /** Reubica un nodo en un punto nuevo del lienzo, como si fuera una estrella distinta apareciendo */
  private respawnNode(node: HeroNode, width: number, height: number): void {
    node.x = Math.random() * width;
    node.y = Math.random() * height;
    node.vx = (Math.random() - 0.5) * 0.28;
    node.vy = (Math.random() - 0.5) * 0.28;
    node.brightness = Math.random();
    node.color = this.nodeColors[node.brightness > 0.5 ? 1 : 0];
  }

  /**
   * Bucle de animación de la red: mueve los nodos, los une entre sí cuando están
   * cerca, garantiza que ningún nodo quede aislado, y convierte al cursor en un
   * nodo más que atrae y conecta con los que tiene alrededor.
   */
  private animateHeroNetwork(canvas: HTMLCanvasElement): void {
    const ctx = this.ctx;
    if (!ctx) return;

    const width = canvas.clientWidth;
    const height = canvas.clientHeight;
    // Radio de enlace entre nodos y de atracción/enlace con el cursor: generosos a propósito,
    // para que la red se sienta conectada y reactiva en vez de dispersa
    const linkDist = 190;
    const mouseLinkDist = Math.max(width, height) * 0.5;

    ctx.clearRect(0, 0, width, height);

    for (const node of this.nodes) {
      if (!this.reducedMotion) {
        // Atracción hacia el cursor: más fuerte cuanto más cerca, y con poca inercia
        // para que la respuesta se note inmediata en vez de con retardo
        if (this.mouse.active) {
          const dx = this.mouse.x - node.x;
          const dy = this.mouse.y - node.y;
          const dist = Math.hypot(dx, dy);
          if (dist > 0 && dist < mouseLinkDist) {
            const pull = (1 - dist / mouseLinkDist) * 0.05;
            node.vx += (dx / dist) * pull;
            node.vy += (dy / dist) * pull;
          }
        }

        node.vx *= 0.92;
        node.vy *= 0.92;
        node.x += node.vx;
        node.y += node.vy;

        if (node.x < 0 || node.x > width) node.vx *= -1;
        if (node.y < 0 || node.y > height) node.vy *= -1;
        node.x = Math.min(Math.max(node.x, 0), width);
        node.y = Math.min(Math.max(node.y, 0), height);

        // Ciclo de vida: cada nodo permanece visible un tiempo, se apaga, reaparece
        // en otro punto del hero y vuelve a encenderse. Evita que la red acabe
        // apelotonada siguiendo al cursor y deje el resto del hero vacío.
        node.timer -= 1;
        if (node.fade === 'visible' && node.timer <= 0) {
          node.fade = 'out';
          node.timer = this.fadeFrames;
        } else if (node.fade === 'out') {
          node.alpha = Math.max(0, node.alpha - 1 / this.fadeFrames);
          if (node.alpha <= 0) {
            this.respawnNode(node, width, height);
            node.fade = 'in';
            node.timer = this.fadeFrames;
          }
        } else if (node.fade === 'in') {
          node.alpha = Math.min(1, node.alpha + 1 / this.fadeFrames);
          if (node.alpha >= 1) {
            node.fade = 'visible';
            node.timer = this.visibleFrames();
          }
        }
      }
    }

    // Conexiones entre nodos cercanos, y registro del vecino más próximo de cada uno
    // para garantizar que ningún nodo quede como una "constelación" aislada
    const nearestDist = new Array(this.nodes.length).fill(Infinity);
    const nearestIdx = new Array(this.nodes.length).fill(-1);

    for (let i = 0; i < this.nodes.length; i++) {
      for (let j = i + 1; j < this.nodes.length; j++) {
        const a = this.nodes[i];
        const b = this.nodes[j];
        const dist = Math.hypot(a.x - b.x, a.y - b.y);

        if (dist < nearestDist[i]) { nearestDist[i] = dist; nearestIdx[i] = j; }
        if (dist < nearestDist[j]) { nearestDist[j] = dist; nearestIdx[j] = i; }

        if (dist < linkDist) {
          ctx.strokeStyle = a.color;
          ctx.globalAlpha = (1 - dist / linkDist) * 0.12 * Math.min(a.alpha, b.alpha);
          ctx.lineWidth = 1;
          ctx.beginPath();
          ctx.moveTo(a.x, a.y);
          ctx.lineTo(b.x, b.y);
          ctx.stroke();
        }
      }
    }

    // Enlace garantizado al vecino más cercano para los nodos que quedaron sin conexión visible
    for (let i = 0; i < this.nodes.length; i++) {
      if (nearestIdx[i] !== -1 && nearestDist[i] >= linkDist) {
        const a = this.nodes[i];
        const b = this.nodes[nearestIdx[i]];
        ctx.strokeStyle = a.color;
        ctx.globalAlpha = 0.06 * Math.min(a.alpha, b.alpha);
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.moveTo(a.x, a.y);
        ctx.lineTo(b.x, b.y);
        ctx.stroke();
      }
    }

    // Conexiones desde el cursor: te conviertes en un nodo más de la red
    if (this.mouse.active) {
      for (const node of this.nodes) {
        const dist = Math.hypot(this.mouse.x - node.x, this.mouse.y - node.y);
        if (dist < mouseLinkDist) {
          ctx.strokeStyle = node.color;
          ctx.globalAlpha = (1 - dist / mouseLinkDist) * 0.28 * node.alpha;
          ctx.lineWidth = 1;
          ctx.beginPath();
          ctx.moveTo(this.mouse.x, this.mouse.y);
          ctx.lineTo(node.x, node.y);
          ctx.stroke();
        }
      }
    }

    // Nodos, con brillo tipo magnitud estelar: mismo tono rojo, distinta intensidad de irradiación
    ctx.globalAlpha = 1;
    for (const node of this.nodes) {
      ctx.save();
      ctx.shadowColor = node.color;
      ctx.shadowBlur = (1.5 + node.brightness * 7) * node.alpha;
      ctx.globalAlpha = (0.45 + node.brightness * 0.55) * node.alpha;
      ctx.beginPath();
      ctx.fillStyle = node.color;
      ctx.arc(node.x, node.y, node.r, 0, Math.PI * 2);
      ctx.fill();
      ctx.restore();
    }

    // Nodo del cursor: la estrella más brillante de la escena mientras esté activo
    if (this.mouse.active) {
      ctx.save();
      ctx.shadowColor = this.nodeColors[1];
      ctx.shadowBlur = 10;
      ctx.beginPath();
      ctx.fillStyle = this.nodeColors[1];
      ctx.arc(this.mouse.x, this.mouse.y, 3, 0, Math.PI * 2);
      ctx.fill();
      ctx.restore();
    }

    this.rafId = requestAnimationFrame(() => this.animateHeroNetwork(canvas));
  }

  /** Registra la posición del cursor sobre el lienzo, en coordenadas del propio canvas */
  onHeroMouseMove(event: MouseEvent, canvas: HTMLCanvasElement): void {
    const rect = canvas.getBoundingClientRect();
    this.mouse.x = event.clientX - rect.left;
    this.mouse.y = event.clientY - rect.top;
    this.mouse.active = true;
  }

  /** Desactiva la interacción cuando el cursor sale del lienzo */
  onHeroMouseLeave(): void {
    this.mouse.active = false;
  }

  /**
   * Array de características principales de la plataforma
   * Cada objeto contiene: icon, title, desc
   */
features = [
  { icon: 'lupa'    as IconName, title: 'Busca viajes',    desc: 'Filtra por campus, localidad, fecha y dirección para encontrar tu pana de viaje perfecto.' },
  { icon: 'coche'   as IconName, title: 'Publica el tuyo', desc: 'Ofrece plazas libres en tu coche, fija un precio y gestiona quién viaja contigo.' },
  { icon: 'estrella' as IconName, title: 'Confirma y viaja', desc: 'Solicita plaza, el conductor acepta y compartes el camino. Así de fácil.' },
];

  /**
   * Hook de ciclo de vida de Angular
   * Se ejecuta al inicializar el componente
   * Carga viajes sugeridos o generales según autenticación
   */
  ngOnInit(): void {
    // Verificar si estamos en el navegador (no en servidor)
    if (!isPlatformBrowser(this.platformId)) { this.loadingTrips = false; return; }

    // Obtener usuario autenticado
    const user = this.auth.getUser();
    if (user?.id) {
      // Usuario logueado: cargar viajes personalizados
      this.isSuggested = true;
      this.api.getSuggestedTrips(user.id, 0, 6).subscribe({
        next: p => {
          this.suggestedTrips = p.content;
          this.isSuggested = p.content.length > 0;
          this.loadingTrips = false;
          this.cdr.detectChanges();
        },
        error: () => { this.loadFutureTrips(); },
      });
    } else {
      // Usuario no logueado: cargar viajes generales futuros
      this.loadFutureTrips();
    }
  }

  /**
   * Carga viajes futuros generales (sin personalización)
   * Se usa como fallback si no hay viajes sugeridos o usuario no autenticado
   * @private
   */
  private loadFutureTrips() {
    this.isSuggested = false;
    this.api.getFutureTrips(0, 6).subscribe({
      next: p => { this.suggestedTrips = p.content; this.loadingTrips = false; this.cdr.detectChanges(); },
      error: () => { this.loadingTrips = false; this.cdr.detectChanges(); },
    });
  }

  /**
   * Actualiza un viaje en la lista cuando cambia
   * Se usa cuando un viaje es modificado por el usuario
   * @param updated - Viaje actualizado
   * @param index - Índice del viaje en el array
   */
  updateTrip(updated: Trip, index: number) {
    this.suggestedTrips[index] = updated;
    this.cdr.detectChanges();
  }

  /**
   * Getter que devuelve el nombre del usuario autenticado
   * Extrae solo el primer nombre si está disponible
   * @returns Primer nombre del usuario o string vacío si no está autenticado
   */
  get userName(): string {
    return this.auth.getUser()?.name?.split(' ')[0] || '';
  }
}