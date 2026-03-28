import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  // Páginas estáticas — se pueden pre-renderizar
  { path: '',              renderMode: RenderMode.Prerender },
  { path: 'faq',           renderMode: RenderMode.Prerender },
  { path: 'about',         renderMode: RenderMode.Prerender },
  { path: 'login',         renderMode: RenderMode.Prerender },
  { path: 'signup',        renderMode: RenderMode.Prerender },
  { path: 'recovery-password', renderMode: RenderMode.Prerender },
  { path: 'reset-password',    renderMode: RenderMode.Prerender },

  // Páginas dinámicas — requieren datos del usuario o del backend en tiempo real
  { path: 'profile',       renderMode: RenderMode.Client },
  { path: 'my-trips',      renderMode: RenderMode.Client },
  { path: 'trip',          renderMode: RenderMode.Client },
  { path: 'settings',      renderMode: RenderMode.Client },
  { path: 'logout',        renderMode: RenderMode.Client },
  { path: 'search-trip',   renderMode: RenderMode.Client },
  { path: 'trip-detail/:id', renderMode: RenderMode.Client },

  // Fallback
  { path: '**',            renderMode: RenderMode.Client },
];
