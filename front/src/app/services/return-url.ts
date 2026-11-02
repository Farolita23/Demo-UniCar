/**
 * Ruta a la que volver tras iniciar sesión o registrarse (`?returnUrl=`).
 * Solo se aceptan rutas internas de la app, para no redirigir a webs externas.
 */
export function safeReturnUrl(value: string | null | undefined): string | null {
    if (!value || !value.startsWith('/') || value.startsWith('//') || value.startsWith('/\\')) return null;
    if (value.startsWith('/login') || value.startsWith('/signup') || value.startsWith('/logout')) return null;
    return value;
}

/** Mensaje de contexto para el login según a dónde quería ir el usuario */
export function returnUrlHint(url: string | null): string {
    if (!url) return '';
    if (url.startsWith('/trip-detail')) return 'Entra para pedir plaza en este viaje.';
    if (url === '/trip' || url.startsWith('/trip?')) return 'Entra para publicar tu viaje.';
    return 'Entra y te dejamos justo donde ibas.';
}
