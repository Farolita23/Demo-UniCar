import { Component, ElementRef, HostListener, Input, forwardRef, inject, ChangeDetectorRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

export interface SearchSelectOption {
    id: number | string;
    name: string;
}

/**
 * Desplegable con barra de búsqueda. Se usa con formControlName y guarda el id
 * de la opción elegida como string (igual que un <select> nativo).
 */
@Component({
    selector: 'app-search-select',
    standalone: true,
    templateUrl: './search-select.html',
    styleUrl: './search-select.css',
    providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => SearchSelect), multi: true }],
})
export class SearchSelect implements ControlValueAccessor {
    @Input() options: SearchSelectOption[] = [];
    @Input() placeholder = 'Seleccionar...';
    @Input() searchPlaceholder = 'Buscar...';

    private host = inject(ElementRef<HTMLElement>);
    private cdr = inject(ChangeDetectorRef);

    readonly String = String;
    value = '';
    query = '';
    open = false;
    disabled = false;
    active = 0;

    private onChange: (v: string) => void = () => { };
    private onTouched: () => void = () => { };

    get selectedName(): string {
        return this.options.find(o => String(o.id) === this.value)?.name ?? '';
    }

    // Filtra ignorando mayúsculas y tildes
    get filtered(): SearchSelectOption[] {
        const q = this.normalize(this.query);
        return q ? this.options.filter(o => this.normalize(o.name).includes(q)) : this.options;
    }

    private normalize(s: string): string {
        return s.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase().trim();
    }

    toggle(): void {
        if (this.disabled) return;
        this.open ? this.close() : this.openPanel();
    }

    openPanel(): void {
        this.open = true;
        this.query = '';
        this.active = Math.max(0, this.filtered.findIndex(o => String(o.id) === this.value));
        // Pintar el panel ya y enfocar el buscador
        this.cdr.detectChanges();
        (this.host.nativeElement as HTMLElement).querySelector<HTMLInputElement>('.ss-search')?.focus();
    }

    close(): void {
        if (!this.open) return;
        this.open = false;
        this.onTouched();
    }

    onQuery(event: Event): void {
        this.query = (event.target as HTMLInputElement).value;
        this.active = 0;
    }

    select(option: SearchSelectOption): void {
        this.value = String(option.id);
        this.onChange(this.value);
        this.close();
    }

    onKeydown(event: KeyboardEvent): void {
        const list = this.filtered;
        if (event.key === 'ArrowDown') { this.active = Math.min(list.length - 1, this.active + 1); event.preventDefault(); this.scrollActive(); }
        else if (event.key === 'ArrowUp') { this.active = Math.max(0, this.active - 1); event.preventDefault(); this.scrollActive(); }
        else if (event.key === 'Enter') { event.preventDefault(); if (list[this.active]) this.select(list[this.active]); }
        else if (event.key === 'Escape') { event.preventDefault(); this.close(); }
    }

    private scrollActive(): void {
        setTimeout(() => (this.host.nativeElement as HTMLElement).querySelector('.ss-option.active')?.scrollIntoView({ block: 'nearest' }));
    }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: Event): void {
        if (this.open && !(this.host.nativeElement as HTMLElement).contains(event.target as Node)) {
            this.close();
            this.cdr.detectChanges();
        }
    }

    // ControlValueAccessor
    writeValue(v: any): void { this.value = v == null ? '' : String(v); this.cdr.markForCheck(); }
    registerOnChange(fn: (v: string) => void): void { this.onChange = fn; }
    registerOnTouched(fn: () => void): void { this.onTouched = fn; }
    setDisabledState(disabled: boolean): void { this.disabled = disabled; }
}
