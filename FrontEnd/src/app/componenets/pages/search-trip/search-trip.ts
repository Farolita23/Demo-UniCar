import { Component } from '@angular/core';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { Trip } from '../../elements/trip/trip';
import { Pageable } from '../../elements/pageable/pageable';
import { ActivatedRoute, Router } from '@angular/router';
import { Rating } from '../../elements/rating/rating';

@Component({
    selector: 'app-search-trip',
    imports: [
        Header,
        Trip,
        Pageable,
        Footer,
        Rating,
    ],
    templateUrl: './search-trip.html',
    styleUrl: './search-trip.css',
})
export class SearchTrip {

    currentPage: number = 1;
    itemsPerPage = 12;

    filter = {
        rating: 0,
    }

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            this.currentPage = params['page'] ? +params['page'] : 1;
        });
        this.filterApply();
    }

    constructor(
        private router: Router,
        private route: ActivatedRoute,
    ) {}
    
    onChangePage(page: number) {
        this.currentPage = page;
        this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { page: page },
            queryParamsHandling: 'merge'
        });
        this.filterApply();
    }
    onChangeRatingFilter(rating: any){
        this.filter.rating = rating;
    }
    filterApply(){
        this.filteredTripList = this.tripList.filter((item, i) => {
            const inPage = i > (this.itemsPerPage * (this.currentPage - 1)) && i <= (this.itemsPerPage * this.currentPage);                   
            return inPage;
        });
    }
    filterReset(){
        this.filterApply();
    }

    tripList = [
        { user: { id: 1, name: 'New York', rating: 0.0 , ratings:  0}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 2, name: 'New York', rating: 0.1 , ratings:  1}, trip: { id: 2, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 3, name: 'New York', rating: 0.2 , ratings:  2}, trip: { id: 3, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 4, name: 'New York', rating: 0.3 , ratings:  3}, trip: { id: 4, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.4 , ratings:  4}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.5 , ratings:  5}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.6 , ratings:  6}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.7 , ratings:  7}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.8 , ratings:  8}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 0.9 , ratings:  9}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 1, name: 'New York', rating: 1.0 , ratings: 10}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 2, name: 'New York', rating: 1.1 , ratings: 11}, trip: { id: 2, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 3, name: 'New York', rating: 1.2 , ratings: 12}, trip: { id: 3, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 4, name: 'New York', rating: 1.3 , ratings: 13}, trip: { id: 4, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.4 , ratings: 14}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.5 , ratings: 15}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.6 , ratings: 16}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.7 , ratings: 17}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.8 , ratings: 18}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 1.9 , ratings: 19}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 1, name: 'New York', rating: 2.0 , ratings: 20}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 2, name: 'New York', rating: 2.1 , ratings: 21}, trip: { id: 2, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 3, name: 'New York', rating: 2.2 , ratings: 22}, trip: { id: 3, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 4, name: 'New York', rating: 2.3 , ratings: 23}, trip: { id: 4, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.4 , ratings: 24}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.5 , ratings: 25}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.6 , ratings: 26}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.7 , ratings: 27}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.8 , ratings: 28}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 2.9 , ratings: 29}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 1, name: 'New York', rating: 3.0 , ratings: 30}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 2, name: 'New York', rating: 3.1 , ratings: 31}, trip: { id: 2, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 3, name: 'New York', rating: 3.2 , ratings: 32}, trip: { id: 3, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 4, name: 'New York', rating: 3.3 , ratings: 33}, trip: { id: 4, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.4 , ratings: 34}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.5 , ratings: 35}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.6 , ratings: 36}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.7 , ratings: 37}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.8 , ratings: 38}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 3.9 , ratings: 39}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 1, name: 'New York', rating: 4.0 , ratings: 40}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 2, name: 'New York', rating: 4.1 , ratings: 41}, trip: { id: 2, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 3, name: 'New York', rating: 4.2 , ratings: 42}, trip: { id: 3, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 4, name: 'New York', rating: 4.3 , ratings: 43}, trip: { id: 4, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.4 , ratings: 44}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.5 , ratings: 45}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.6 , ratings: 46}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.7 , ratings: 47}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.8 , ratings: 48}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 5, name: 'New York', rating: 4.9 , ratings: 49}, trip: { id: 5, from: "Los palacios", dest: "Utrera" }},
        { user: { id: 1, name: 'New York', rating: 5.0 , ratings: 50}, trip: { id: 1, from: "Los palacios", dest: "Utrera" }},
    ];

    filteredTripList: any[] = [];

}
