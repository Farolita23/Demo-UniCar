import { Pipe, PipeTransform } from '@angular/core';
import { env } from '../../environments/env';

/**
 * Convierte el valor de `profileImageUrl` en una URL lista para un <img>.
 * Las fotos propias llegan como ruta relativa (`/uploads/avatars/...`) y se completan con la URL
 * de la API; las externas (p. ej. la foto de Google) y las vistas previas locales se dejan igual.
 */
@Pipe({ name: 'avatarUrl', standalone: true })
export class AvatarUrlPipe implements PipeTransform {
    transform(url?: string | null): string | null {
        if (!url) return null;
        return url.startsWith('/uploads/') ? env.API_SAME_URL + url : url;
    }
}
