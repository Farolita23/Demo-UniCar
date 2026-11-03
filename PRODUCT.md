# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

- **Pasajeros:** estudiantes y profesores de centros de estudio de Sevilla que necesitan llegar a su campus desde su localidad y buscan un trayecto compartido más barato y cómodo que ir solos o en transporte público.
- **Conductores:** miembros de la misma comunidad que hacen ese trayecto con su coche y quieren compartir gastos llenando plazas libres, de forma puntual o de forma recurrente (mismo horario cada semana).
- **Administradores:** moderan la comunidad: revisan reportes, ponen strikes y banean o desbanean usuarios.

Uso típico desde el móvil, poco antes de salir hacia el campus o al organizar la semana.

## Product Purpose

UniCar permite que estudiantes y profesores coordinen trayectos compartidos al campus: el conductor publica un viaje con precio por plaza y los pasajeros lo buscan, lo solicitan y el conductor acepta o rechaza.

Es un producto pensado para lanzarse con usuarios reales en Sevilla y, a la vez, una pieza de portfolio de su equipo. El éxito es doble: que la comunidad publique y complete viajes de verdad, y que la app se vea y funcione con nivel profesional en una demo.

## Positioning

Carpooling hiperlocal y cerrado a la comunidad universitaria de Sevilla: los trayectos se organizan por campus de destino y localidad de origen, no por ciudades, y la confianza viene de ser compañeros del mismo entorno más el sistema de valoraciones, reportes y strikes. Admite trayectos periódicos (plantilla de horario de la que se generan los viajes) pensados para la rutina semanal de clases.

## Operating Context

- **Campus de destino:** Reina Mercedes, Viapol / Ramón y Cajal, Cartuja, Macarena, Politécnico, Rectorado / Centro, UPO (Pablo de Olavide) y Bellas Artes.
- **Origen:** una de las ~100 localidades de la provincia de Sevilla registradas en la app.
- **Flujo del pasajero:** buscar viaje (por campus, localidad, fecha) → ver detalle → solicitar plaza → el conductor acepta o rechaza → viajar → valorar.
- **Flujo del conductor:** registrar su coche → publicar un viaje suelto o periódico → gestionar solicitudes y pasajeros → valorar.
- **Confianza:** valoraciones entre conductor y pasajeros, reportes, strikes (3 strikes = baneo automático) y favoritos.
- **Acceso:** registro con usuario y contraseña o con cuenta de Google (registro guiado por pasos).

## Capabilities and Constraints

- Frontend Angular (standalone), backend Spring Boot con API REST y JWT, MySQL; todo levantable con Docker Compose.
- Precio por plaza obligatorio, mínimo 0,50 €. La app no gestiona pagos: el pago entre usuarios queda fuera de ella.
- Fotos de perfil subidas por el usuario (recortadas a cuadrado) o la foto de la cuenta de Google.
- Roles: usuario y administrador.
- Ámbito geográfico actual: solo Sevilla; ampliarlo es una posibilidad futura, no un compromiso.
- **Sin decidir:** si la app será siempre gratuita y sin anuncios. El texto actual de "Nosotros" lo dice, pero no está confirmado como compromiso.

## Brand Commitments

- El nombre **UniCar** y el logo actual se mantienen: el wordmark tipográfico «Uni**Car**» con «Car» en Rojo Piloto (y el favicon «UC» a juego). El antiguo dibujo del coche rojo se retiró del proyecto y no debe volver a usarse.
- **Voz sevillana y gamberra:** tuteo, humor local y desenfadado como seña de identidad (por ejemplo "Ve con un notas a la universidad"). El humor no debe tapar la información que el usuario necesita para actuar (fechas, precios, plazas, errores).

## Evidence on Hand

- Documentación del proyecto: `DocumentacionUnicar.pdf` y `README.md`.
- Datos de ejemplo en `unicardb.sql` (usuarios, viajes y valoraciones ficticios para desarrollo).
- No hay todavía usuarios reales, testimonios, cifras de uso ni prensa: no deben inventarse.

## Product Principles

1. **Llegar a clase es lo primero:** encontrar o publicar un viaje debe ser rápido desde el móvil; todo lo demás es secundario.
2. **Confianza entre compañeros:** la identidad, las valoraciones y las normas (strikes, reportes) deben estar siempre a la vista donde se decide subirse a un coche.
3. **La rutina manda:** los trayectos se repiten cada semana; el producto favorece lo periódico y lo habitual frente a lo excepcional.
4. **Con gracia, pero claro:** la personalidad sevillana está en el tono, nunca a costa de la claridad de datos y acciones.

## Accessibility & Inclusion

- Objetivo WCAG 2.1 AA: contraste suficiente, uso completo con teclado, foco visible y etiquetas accesibles para lectores de pantalla.
- Diseño pensado primero para móvil.
- Interfaz en español (`lang="es"`).
