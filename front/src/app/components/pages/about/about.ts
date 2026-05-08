/**
 * Componente de la página "Acerca de"
 * 
 * Esta página proporciona información sobre la aplicación UniCar,
 * su propósito y funcionalidades principales.
 */

// Importaciones de Angular
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';

/**
 * Componente About
 * 
 * Define la estructura y comportamiento de la página "Acerca de".
 * Utiliza componentes reutilizables como Header y Footer.
 */
@Component({
    // Selector CSS para usar el componente en templates
    selector: 'page-about',
    // Componente standalone sin necesidad de módulo
    standalone: true,
    // Módulos y componentes importados
    imports: [CommonModule, Header, Footer, AppIcon],
    // Archivo HTML de la plantilla
    templateUrl: './about.html',
    // Archivo CSS de estilos
    styleUrl: './about.css',
})
export class About { }
