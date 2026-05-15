/**
 * Componente de Página de Preguntas Frecuentes (FAQ)
 * 
 * Muestra un acordeón interactivo con preguntas y respuestas frecuentes
 * sobre el funcionamiento de la aplicación UniCar.
 */

// Importaciones de Angular
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

/**
 * Componente Faq
 * 
 * Gestiona la visualización de preguntas frecuentes en un formato
 * de acordeón expandible. Los usuarios pueden hacer clic en cualquier
 * pregunta para ver/ocultar su respuesta.
 */
@Component({
    // Selector CSS para usar el componente en templates
    selector: 'page-faq',
    // Componente standalone sin necesidad de módulo
    standalone: true,
    // Módulos y componentes importados
    imports: [CommonModule, Header, Footer],
    // Archivo HTML de la plantilla
    templateUrl: './faq.html',
    // Archivo CSS de estilos
    styleUrl: './faq.css',
})
export class Faq {
    /**
     * Índice del item FAQ actualmente abierto
     * Valor -1 significa que ninguno está abierto
     */
    openIndex = -1;
    
    /**
     * Alterna entre abrir y cerrar un item FAQ
     * Si el item ya está abierto, lo cierra; si está cerrado, lo abre
     * @param i - Índice del item FAQ a alternar
     */
    toggle(i: number) { this.openIndex = this.openIndex === i ? -1 : i; }

    /**
     * Arreglo de preguntas y respuestas frecuentes
     * Cada objeto contiene:
     * - q: pregunta que se muestra en el acordeón
     * - a: respuesta que se expande al hacer clic
     */
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
