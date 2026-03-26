import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'app-pageable',
    imports: [],
    templateUrl: './pageable.html',
    styleUrl: './pageable.css',
})
export class Pageable {

    @Input() cur = 1;
    @Input() max = 20;
    @Input() maxItems = 7;
    @Output() eventChangeCurrent = new EventEmitter<number>();

    changeCur(cur: number) {
        if(cur < 1 || cur > this.max) return;
        this.cur = cur;
        this.eventChangeCurrent.emit(cur);
    }

    get pages(): number[] {
        const half = Math.floor(this.maxItems / 2);
        let start = this.cur - half;
        let end = this.cur + half;

        if (start < 1) {
            start = 1;
            end = Math.min(this.maxItems, this.max);
        }

        if (end > this.max) {
            end = this.max;
            start = Math.max(1, end - this.maxItems + 1);
        }

        return Array.from({ length: end - start + 1 }, (_, i) => start + i);
    }
}
