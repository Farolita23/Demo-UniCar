import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
    selector: 'page-faq',
    standalone: true,
    imports: [CommonModule, Header, Footer],
    templateUrl: './faq.html',
    styleUrl: './faq.css',
})
export class Faq {

    openIndex = -1;
    
    toggle(i: number) { this.openIndex = this.openIndex === i ? -1 : i; }

    items = [
        {
            q: '¿Cómo me registro en UniCar?',
            a: 'Haz clic en "Únete" en la cabecera, rellena el formulario con tus datos personales, tu campus habitual y tu localidad de origen.'
        }, {
            q: '¿Cómo busco un viaje?',
            a: 'Desde la sección "Buscar viaje" puedes filtrar por campus, localidad, fecha y precio máximo para encontrar el trayecto que mejor se adapte a ti.'
        }, {
            q: '¿Cómo publico mi propio viaje?',
            a: 'Primero registra tu vehículo en tu perfil. Luego accede a "Publicar viaje", selecciona el coche, la ruta, la fecha y el precio por plaza.'
        }, {
            q: '¿Cómo funciona el sistema de solicitudes?',
            a: 'Al solicitar una plaza, el conductor recibe una notificación y puede aceptarte o rechazarte. Una vez aceptado, quedas confirmado como pasajero.'
        }, {
            q: '¿Puedo cancelar mi participación en un viaje?',
            a: 'Sí. Desde los detalles del viaje puedes abandonarlo si ya eras pasajero, o cancelar tu solicitud si todavía está pendiente de aprobación.'
        }, {
            q: '¿Qué son los "strikes"?',
            a: 'Los strikes son advertencias que reciben los usuarios por comportamiento inadecuado. Acumular varios strikes puede resultar en la suspensión de la cuenta.'
        }, {
            q: '¿Puedo valorar al conductor o pasajero?',
            a: 'Sí. Después de compartir un viaje con alguien puedes dejarle una valoración de 1 a 5 estrellas para ayudar a la comunidad.'
        }, {
            q: '¿Es seguro usar UniCar?',
            a: 'Todos los usuarios se registran con sus datos reales. Además, el sistema de valoraciones y reportes permite mantener la calidad y seguridad de la comunidad.'
        },
    ];
}
