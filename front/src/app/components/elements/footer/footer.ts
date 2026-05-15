import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth-service';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../services/api-service';

@Component({
    selector: 'app-footer',
    standalone: true,
    imports: [RouterLink, FormsModule],
    templateUrl: './footer.html',
    styleUrl: './footer.css',
})
export class Footer {
    auth = inject(AuthService);
    api = inject(ApiService);
    year = new Date().getFullYear();

    get isAdmin(): boolean {
        return this.auth.getUser()?.role === 'ADMIN';
    }
    
}
