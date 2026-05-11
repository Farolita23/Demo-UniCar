import { Component, inject, OnInit, ChangeDetectorRef, NgZone, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { User } from '../../../models/user.model';
import { Car } from '../../../models/car.model';
import { Trip } from '../../../models/trip.model';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';

// Validador personalizado para el formato de matrícula de coche
function plateValidator(control: AbstractControl): ValidationErrors | null {
    const v: string = (control.value || '').trim();
    const errors: ValidationErrors = {};

    const regex = /^(\d{4}\s?[BCDFGHJKLMNPRSTVWXYZ]{3}|[A-Z]{1,2}-\d{4}-[A-Z]{1,2})$/;

    if (!regex.test(v)) { errors['invalidFormat'] = true; }

    return Object.keys(errors).length ? errors : null;
}

@Component({
    selector: 'page-profile',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer, TripCard, AppIcon],
    templateUrl: './profile.html',
    styleUrl: './profile.css',
})
export class Profile implements OnInit {

    // Inyección de servicios
    fb = inject(FormBuilder);
    api = inject(ApiService);
    auth = inject(AuthService);
    cdr = inject(ChangeDetectorRef);
    zone = inject(NgZone);
    platformId = inject(PLATFORM_ID);

    // Datos del usuario y relacionados
    user: User | null = null;
    cars: Car[] = [];
    tripsAsDriver: Trip[] = [];
    tripsAsPassenger: Trip[] = [];
    campuses: Campus[] = [];
    towns: Town[] = [];

    // Control de pestañas
    activeTab: 'info' | 'trips-driver' | 'trips-passenger' | 'cars' | 'ratings' | 'edit' = 'info';

    // Estados de carga y guardado
    loading = true;
    carsLoading = false;
    saving = false;

    // Estados y controles para edición de perfil
    editSuccess = false;
    editError = '';
    editPreviewUrl: string | null = null;

    // Estados y controles para gestión de vehículos
    showCarModal = false;
    carSaving = false;
    carError = '';

    // Control para formulario de nuevo vehículo
    carForm = this.fb.group({
        model: ['', Validators.required],
        color: ['', Validators.required],
        licensePlate: ['', [Validators.required, plateValidator]],
        capacity: [4, [Validators.required, Validators.min(2), Validators.max(9)]],
    });

    // Control para formulario de edición de perfil
    editForm = this.fb.group({
        name: ['', Validators.required],
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(16)]],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
        description: [''],
        profileImageUrl: [''],
        idUsualCampus: ['' as string],
        idHomeTown: ['' as string],
    });
    
    // Inicialización del componente: carga datos necesarios para mostrar el perfil
    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) {
            this.loading = false;
            return;
        }

        this.api.getCampuses().subscribe({
            next: c => { this.campuses = c; this.cdr.detectChanges(); },
            error: e => console.error('[Profile] getCampuses error:', e),
        });
        this.api.getTowns().subscribe({
            next: t => { this.towns = t; this.cdr.detectChanges(); },
            error: e => console.error('[Profile] getTowns error:', e),
        });

        const stored = this.auth.getUser();
        if (stored?.id) {
            this.loadUser(stored.id);
        } else {
            this.loading = false;
            this.cdr.detectChanges();
        }
    }

    // Carga los datos del usuario desde la API y los guarda en el estado del componente
    loadUser(id: number) {
        this.loading = true;
        this.api.getUser(id).subscribe({
            next: u => {
                this.user = u;
                this.auth.saveUser(u);
                this.populateEditForm(u);
                this.loading = false;
                this.loadCars(id);
                this.loadTrips(id);
                this.cdr.detectChanges();
            },
            error: e => {
                console.error('[Profile] getUser error:', e?.status, e?.error);
                this.loading = false;
                this.cdr.detectChanges();
            },
        });
    }

    // Carga los coches del usuario para mostrarlos en su perfil
    loadCars(userId: number) {
        this.carsLoading = true;
        this.api.getCarsByUser(userId).subscribe({
            next: cars => {
                this.cars = cars;
                this.carsLoading = false;
                this.cdr.detectChanges();
            },
            error: e => {
                console.error('[Profile] getCarsByUser error:', e?.status, e?.error);
                this.carsLoading = false;
                this.cdr.detectChanges();
            },
        });
    }

    // Carga los viajes del usuario tanto como conductor como pasajero
    loadTrips(id: number) {
        this.api.getTripsAsDriver(id).subscribe({
            next: p => { this.tripsAsDriver = p.content; this.cdr.detectChanges(); },
            error: e => console.error('[Profile] getTripsAsDriver error:', e),
        });
        this.api.getTripsAsPassenger(id).subscribe({
            next: p => { this.tripsAsPassenger = p.content; this.cdr.detectChanges(); },
            error: e => console.error('[Profile] getTripsAsPassenger error:', e),
        });
    }

    // Rellenar el formulario de edición con los datos del usuario
    populateEditForm(u: User) {
        this.editForm.patchValue({
            username: u.username,
            email: u.email,
            name: u.name,
            phone: u.phone,
            description: u.description || '',
            profileImageUrl: u.profileImageUrl || '',
            idUsualCampus: u.usualCampusDTO?.id != null ? String(u.usualCampusDTO.id) : '',
            idHomeTown: u.homeTownDTO?.id != null ? String(u.homeTownDTO.id) : '',
        });
    }

    // Manejar selección de nueva imagen de perfil y mostrar vista previa
    onProfileFileSelected(event: Event) {
        const file = (event.target as HTMLInputElement).files?.[0];
        if (!file) return;
        if (file.size > 5 * 1024 * 1024) { this.editError = 'La imagen no debe superar los 5MB.'; return; }
        const reader = new FileReader();
        reader.onload = (e) => {
            this.zone.run(() => {
                const result = e.target?.result as string;
                this.editPreviewUrl = result;
                this.editForm.get('profileImageUrl')?.setValue(result);
                this.cdr.detectChanges();
            });
        };
        reader.readAsDataURL(file);
    }

    // Guardar cambios en el perfil del usuario
    editSubmitted = false;
    saveProfile() {
        this.editSubmitted = true;
        if (this.editForm.invalid || !this.user) return;
        this.saving = true; this.editError = ''; this.editSuccess = false;
        const v = this.editForm.value;
        const dto = {
            username: v.username,
            email: v.email,
            name: v.name,
            phone: v.phone,
            description: v.description,
            profileImageUrl: v.profileImageUrl,
            idUsualCampus: v.idUsualCampus ? Number(v.idUsualCampus) : null,
            idHomeTown: v.idHomeTown ? Number(v.idHomeTown) : null,
            birthdate: this.user.birthdate,
            genre: this.user.genre,
            drivingLicenseYear: this.user.drivingLicenseYear,
            password: '',
        };
        this.api.updateUser(this.user.id, dto).subscribe({
            next: u => {
                this.user = u;
                this.auth.saveUser(u);
                this.editSuccess = true;
                this.saving = false;
                this.cdr.detectChanges();
            },
            error: e => {
                this.editError = e?.error?.message || 'Error al guardar.';
                this.saving = false;
                this.cdr.detectChanges();
            },
        });
    }

    // Abrir modal para agregar un nuevo coche
    openCarModal() {
        this.carError = '';
        this.carForm.reset({ capacity: 4 });
        this.showCarModal = true;
    }

    // Guardar un nuevo coche en el perfil
    addCarSubmitted = false;
    addCar() {
        this.addCarSubmitted = true;

        if (this.carForm.invalid || !this.user || this.carSaving) return;
        this.carSaving = true;
        this.carError = '';
        const v = this.carForm.value;
        const dto = {
            model: v.model!,
            color: v.color!,
            licensePlate: v.licensePlate!,
            capacity: Number(v.capacity),
            idDriver: this.user.id,
        };
        this.api.createCar(dto).subscribe({
            next: car => {
                this.cars = [...this.cars, car];
                this.carSaving = false;
                this.showCarModal = false;
                this.cdr.detectChanges();
            },
            error: e => {
                this.carError = e?.error?.message || 'Error al registrar el vehículo.';
                this.carSaving = false;
                this.cdr.detectChanges();
            },
        });
    }

    // Eliminar un coche del perfil
    deleteCar(carId: number) {
        if (!confirm('¿Eliminar este vehículo?')) return;
        this.api.deleteCar(carId).subscribe({
            next: () => {
                this.cars = this.cars.filter(c => c.id !== carId);
                this.cdr.detectChanges();
            },
            error: e => {
                alert(e.error.message);
                console.error('[Profile] deleteCar error:', e);
            },
        });
    }

    // Getter para calcular la valoración media del usuario
    get avgRating(): string {
        const r = this.user?.ratingsReceivedDTO;
        if (!r || r.length === 0) return '—';
        const avg = r.reduce((s, x) => s + x.rating, 0) / r.length;
        return avg.toFixed(1);
    }

    // Método genérico para actualizar un viaje en la lista correspondiente
    updateTrip(updated: Trip, list: Trip[], index: number) { 
        list[index] = updated; 
    }
}
