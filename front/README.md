# UniCar Frontend — Rediseño Completo

## Descripción
Aplicación Angular para la plataforma de carpooling universitario **UniCar**. Rediseño completo con tema oscuro, tipografía premium y todas las funcionalidades del backend implementadas.

## Tecnologías
- Angular 19+ (Standalone Components)
- TypeScript
- CSS Variables (sin SCSS)
- Google Fonts: Syne + DM Sans

## Diseño
- **Tema**: Dark mode con acento rojo (#E63946)
- **Tipografía**: Syne (headings) + DM Sans (body)
- **Responsive**: Mobile-first con breakpoints en 640px, 900px

## Páginas implementadas

| Ruta | Componente | Auth |
|------|-----------|------|
| `/` | Home | No |
| `/search-trip` | SearchTrip | No |
| `/login` | Login | No (solo si no logueado) |
| `/signup` | Signup | No (solo si no logueado) |
| `/recovery-password` | RecoveryPassword | No |
| `/profile` | Profile | Sí |
| `/trip` | PageTrip (publicar viaje) | Sí |
| `/settings` | Settings | Sí |
| `/logout` | Logout | Sí |
| `/about` | About | No |
| `/faq` | FAQ | No |

## Funcionalidades implementadas
- ✅ Búsqueda y filtrado de viajes (campus, localidad, dirección, fecha, precio, plazas)
- ✅ Solicitar/cancelar plaza en un viaje
- ✅ Abandonar viaje como pasajero
- ✅ Publicar viaje propio
- ✅ Perfil completo con historial (conductor y pasajero)
- ✅ Gestión de vehículos (añadir/eliminar)
- ✅ Editar perfil con foto por URL
- ✅ Registro completo con todos los campos del backend
- ✅ Guards de autenticación

## Instalación y ejecución
```bash
npm install
ng serve
```

La aplicación se conecta por defecto a `http://localhost:8080`. Modifica `src/environments/env.ts` para cambiar la URL del API.

## Configuración
Edita `src/environments/env.ts`:
```ts
export const env = {
  API_URL: 'http://localhost:8080'
};
```
