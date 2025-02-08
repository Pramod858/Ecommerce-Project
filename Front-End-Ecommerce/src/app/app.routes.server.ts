import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '**',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'product/:id',
    renderMode: RenderMode.Server
  },
  {
    path: 'order/:id',
    renderMode: RenderMode.Server
  },
];
