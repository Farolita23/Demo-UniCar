import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-rating',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './rating.html',
    styleUrls: ['./rating.css'],
})
export class Rating implements OnChanges {
    @Input() value: number = 0; // valor recibido del padre

    min = 0;
    max = 5;
    @Input() step = 0.1;
    @Input() readonly: boolean = false;
    @Output() changeValue = new EventEmitter<number>();

    rangePct = '0%'; // porcentaje para el CSS

    ngOnChanges(changes: SimpleChanges) {
        if (changes['value']) {
            this.updateRange(); // recalcular porcentaje al recibir un nuevo valor
        }
    }

    handleInput() {
        if(this.readonly) return
        this.updateRange();
    }

    updateRange() {
        this.changeValue.emit(this.value);
        
        const pct = ((this.value - this.min) / (this.max - this.min)) * 100;
        this.rangePct = pct + '%';
    }
}