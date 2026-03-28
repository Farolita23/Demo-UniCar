import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth-service';
import { env } from '../../environments/env';
import { Trip, Page } from '../models/trip.model';
import { User } from '../models/user.model';
import { Campus } from '../models/campus.model';
import { Town } from '../models/town.model';
import { Car } from '../models/car.model';

@Injectable({ providedIn: 'root' })
export class ApiService {

    readonly URL = env.API_URL;

    // Cabeceras base sin token (para endpoints públicos que necesitan Content-Type)
    private readonly jsonHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(
        private http: HttpClient,
        private authService: AuthService
    ) { }

    private headers(): HttpHeaders {
        let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        const token = this.authService.getToken();
        if (token) {
            headers = headers.set('Authorization', 'Bearer ' + token);
        }
        return headers;
    }

    // AUTH
    login(username: string, password: string): Observable<{ token: string }> {
        return this.http.post<{ token: string }>(
            `${this.URL}/api/auth/login`,
            { username, password },
            { headers: this.jsonHeaders }
        );
    }

    getMe(): Observable<User> {
        return this.http.get<User>(`${this.URL}/api/auth/me`, { headers: this.headers() });
    }

    register(dto: any): Observable<User> {
        return this.http.post<User>(
            `${this.URL}/api/auth/register`,
            dto,
            { headers: this.jsonHeaders }
        );
    }

    // USERS
    getUser(id: number): Observable<User> {
        return this.http.get<User>(`${this.URL}/api/user/${id}`, { headers: this.headers() });
    }

    updateUser(id: number, dto: any): Observable<User> {
        return this.http.put<User>(`${this.URL}/api/user/${id}`, dto, { headers: this.headers() });
    }

    getUserByUsername(username: string): Observable<User[]> {
        return this.http.get<User[]>(`${this.URL}/api/user`, { headers: this.headers() });
    }

    // TRIPS
    getTrips(page = 0, size = 10): Observable<Page<Trip>> {
        const params = new HttpParams().set('page', page).set('size', size).set('sort', 'departureDate,asc');
        return this.http.get<Page<Trip>>(`${this.URL}/api/trip`, { params });
    }

    getTripById(id: number): Observable<Trip> {
        return this.http.get<Trip>(`${this.URL}/api/trip/${id}`);
    }

    searchTrips(filters: any, page = 0, size = 10): Observable<Page<Trip>> {
        const params = new HttpParams().set('page', page).set('size', size).set('sort', 'departureDate,asc');
        return this.http.post<Page<Trip>>(
            `${this.URL}/api/trip/search`,
            filters,
            { headers: this.jsonHeaders, params }
        );
    }

    createTrip(dto: any): Observable<Trip> {
        return this.http.post<Trip>(`${this.URL}/api/trip`, dto, { headers: this.headers() });
    }

    updateTrip(id: number, dto: any): Observable<Trip> {
        return this.http.put<Trip>(`${this.URL}/api/trip/${id}`, dto, { headers: this.headers() });
    }

    deleteTrip(id: number): Observable<void> {
        return this.http.delete<void>(`${this.URL}/api/trip/${id}`, { headers: this.headers() });
    }

    getTripsAsDriver(driverId: number, page = 0): Observable<Page<Trip>> {
        const params = new HttpParams().set('page', page).set('size', 10);
        return this.http.get<Page<Trip>>(`${this.URL}/api/trip/as-driver/${driverId}`, { headers: this.headers(), params });
    }

    getTripsAsPassenger(passengerId: number, page = 0): Observable<Page<Trip>> {
        const params = new HttpParams().set('page', page).set('size', 10);
        return this.http.get<Page<Trip>>(`${this.URL}/api/trip/as-passenger/${passengerId}`, { headers: this.headers(), params });
    }

    getRecommendedTrips(userId: number, page = 0): Observable<Page<Trip>> {
        const params = new HttpParams().set('page', page).set('size', 10);
        return this.http.get<Page<Trip>>(`${this.URL}/api/trip/recommended/${userId}`, { headers: this.headers(), params });
    }

    // JOIN / LEAVE
    requestJoinTrip(tripId: number, userId: number): Observable<Trip> {
        return this.http.post<Trip>(`${this.URL}/api/trip/${tripId}/request/${userId}`, {}, { headers: this.headers() });
    }

    cancelJoinRequest(tripId: number, userId: number): Observable<Trip> {
        return this.http.delete<Trip>(`${this.URL}/api/trip/${tripId}/request/${userId}`, { headers: this.headers() });
    }

    acceptPassenger(tripId: number, requesterId: number): Observable<Trip> {
        return this.http.post<Trip>(`${this.URL}/api/trip/${tripId}/accept/${requesterId}`, {}, { headers: this.headers() });
    }

    rejectPassenger(tripId: number, requesterId: number): Observable<Trip> {
        return this.http.delete<Trip>(`${this.URL}/api/trip/${tripId}/reject/${requesterId}`, { headers: this.headers() });
    }

    leaveTrip(tripId: number, userId: number): Observable<Trip> {
        return this.http.delete<Trip>(`${this.URL}/api/trip/${tripId}/leave/${userId}`, { headers: this.headers() });
    }

    // CARS
    getCars(): Observable<Car[]> {
        return this.http.get<Car[]>(`${this.URL}/api/car`, { headers: this.headers() });
    }

    createCar(dto: any): Observable<Car> {
        return this.http.post<Car>(`${this.URL}/api/car`, dto, { headers: this.headers() });
    }

    updateCar(id: number, dto: any): Observable<Car> {
        return this.http.put<Car>(`${this.URL}/api/car/${id}`, dto, { headers: this.headers() });
    }

    deleteCar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.URL}/api/car/${id}`, { headers: this.headers() });
    }

    // CAMPUS & TOWNS
    getCampuses(): Observable<Campus[]> {
        return this.http.get<Campus[]>(`${this.URL}/api/campus`);
    }

    getTowns(): Observable<Town[]> {
        return this.http.get<Town[]>(`${this.URL}/api/town`);
    }

    // RATINGS
    createRating(dto: { rating: number; idUserRate: number; idRatedUser: number }): Observable<any> {
        return this.http.post<any>(`${this.URL}/api/rating`, dto, { headers: this.headers() });
    }

    // REPORTS
    createReport(dto: { reason: string; idUserReport: number; idReportedUser: number; date: string }): Observable<any> {
        return this.http.post<any>(`${this.URL}/api/report`, dto, { headers: this.headers() });
    }
}
