---
name: UniCar
description: Carpooling entre compañeros hacia los campus de Sevilla
colors:
  rojo-piloto: "#E63946"
  rojo-piloto-hondo: "#C1121F"
  rojo-piloto-claro: "#FF6B6B"
  ambar-intermitente: "#FFB703"
  ambar-intermitente-hondo: "#FB8500"
  noche: "#0D0D0D"
  noche-2: "#141414"
  noche-3: "#1C1C1C"
  asfalto: "#222222"
  asfalto-claro: "#2A2A2A"
  linea: "rgba(255, 255, 255, 0.07)"
  texto: "#F2F2F2"
  texto-2: "#A0A0A0"
  texto-3: "#666666"
  verde-plaza: "#06D6A0"
typography:
  display:
    fontFamily: "Syne, sans-serif"
    fontSize: "64px"
    fontWeight: 700
    lineHeight: 1.05
  headline:
    fontFamily: "Syne, sans-serif"
    fontSize: "clamp(1.8rem, 4vw, 2.8rem)"
    fontWeight: 700
    lineHeight: 1.15
  title:
    fontFamily: "Syne, sans-serif"
    fontSize: "24px"
    fontWeight: 700
    lineHeight: 1.15
  body:
    fontFamily: "DM Sans, sans-serif"
    fontSize: "16px"
    fontWeight: 400
  label:
    fontFamily: "DM Sans, sans-serif"
    fontSize: "14px"
    fontWeight: 600
    letterSpacing: "1px"
rounded:
  sm: "8px"
  md: "16px"
  lg: "24px"
  pill: "100px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.rojo-piloto}"
    textColor: "#FFFFFF"
    rounded: "{rounded.md}"
    padding: "0.7rem 1.5rem"
  button-primary-hover:
    backgroundColor: "{colors.rojo-piloto-hondo}"
  button-ghost:
    backgroundColor: "transparent"
    textColor: "{colors.texto}"
    rounded: "{rounded.md}"
    padding: "0.7rem 1.5rem"
  button-accent:
    backgroundColor: "{colors.ambar-intermitente}"
    textColor: "{colors.noche}"
    rounded: "{rounded.md}"
    padding: "0.7rem 1.5rem"
  button-accent-hover:
    backgroundColor: "{colors.ambar-intermitente-hondo}"
  input:
    backgroundColor: "{colors.asfalto}"
    textColor: "{colors.texto}"
    rounded: "{rounded.sm}"
    padding: "8px 16px"
  card:
    backgroundColor: "{colors.asfalto}"
    rounded: "{rounded.md}"
    padding: "1.5rem"
  badge:
    rounded: "{rounded.pill}"
    padding: "4px 12px"
---

# Design System: UniCar

## Overview

**Creative North Star: "La peña del coche"**

UniCar es la pandilla que comparte coche cada mañana camino del campus: cercana, de barrio y con guasa. El fondo es casi negro, como la carretera a primera hora, para que brillen la gente y los viajes. Sobre él, el Rojo Piloto marca lo importante y el Ámbar Intermitente avisa de lo que pide atención. Los titulares van anchos y con carácter en Syne, y el texto corrido es limpio y legible en DM Sans.

La interfaz es oscura y de capas: no hay superficies claras. La profundidad se construye con escalones de gris sobre el negro y con un brillo rojo suave cuando algo se puede tocar. La personalidad está en el tono y en la interacción, que tiende a lo juguetón: los elementos responden, suben y se iluminan al pasar por encima. La información del viaje (origen, campus, hora, precio, plazas) nunca compite con la decoración.

**Key Characteristics:**
- Tema oscuro único, de capas grises sobre negro casi puro.
- Un solo acento fuerte (Rojo Piloto) y un acento secundario de aviso (Ámbar Intermitente).
- Titulares anchos en Syne; cuerpo en DM Sans.
- Formas redondeadas: tarjetas y botones a 16px, chips en píldora.
- Interacción viva: elevación de 1-2px y brillo rojo suave en hover.
- Pensado primero para móvil.

## Colors

Una noche casi negra con dos luces de coche: el rojo de los pilotos traseros y el ámbar del intermitente.

### Primary
- **Rojo Piloto** (`rojo-piloto`): la acción principal (Buscar viaje, Únete, Acceder), enlaces, pestaña activa, foco de los campos, marca obligatoria de los labels y el resaltado de los titulares ("hacia la uni"). También es el color de peligro: el mismo rojo avisa de errores y plazas agotadas.
- **Rojo Piloto Hondo** (`rojo-piloto-hondo`): estado hover/pulsado de los botones rojos.
- **Rojo Piloto Claro** (`rojo-piloto-claro`): hover de enlaces y textos de error sobre fondo oscuro.

### Secondary
- **Ámbar Intermitente** (`ambar-intermitente`): botón de acento, la valoración con estrella y los avisos (estado "warning"). Lleva texto oscuro encima.
- **Ámbar Intermitente Hondo** (`ambar-intermitente-hondo`): hover del botón ámbar.

### Tertiary
- **Verde Plaza** (`verde-plaza`): estados de éxito, plazas libres y la etiqueta "Hacia campus".

### Neutral
- **Noche** (`noche`): fondo de la página.
- **Noche 2 / Noche 3** (`noche-2`, `noche-3`): franjas y secciones que se separan del fondo.
- **Asfalto** (`asfalto`): tarjetas, campos de formulario y paneles.
- **Asfalto Claro** (`asfalto-claro`): campo enfocado, chips neutros y superficies elevadas.
- **Línea** (`linea`): bordes y divisores, casi invisibles.
- **Texto / Texto 2 / Texto 3** (`texto`, `texto-2`, `texto-3`): texto principal, secundario (labels, subtítulos) y placeholders.

### Named Rules
**The Una Luz Roja Rule.** En cada vista hay una única acción principal en Rojo Piloto; el resto de botones son fantasma o ámbar. Si dos botones rojos compiten, uno sobra.

**The Ámbar Avisa Rule.** El Ámbar Intermitente señala atención (valoración, avisos, acento puntual); no se usa como relleno decorativo ni para acciones destructivas.

## Typography

**Display Font:** Syne (con sans-serif)
**Body Font:** DM Sans (con sans-serif)

**Character:** Syne es ancha, geométrica y algo gamberra: pone el carácter en los titulares. DM Sans es neutra y muy legible, y carga con todos los datos del viaje.

### Hierarchy
- **Display** (700, 64px, 48px en móvil, interlineado 1.05): el titular de la portada.
- **Headline** (700, clamp de 1.8rem a 2.8rem, 1.15): títulos de sección ("Viajes disponibles", "¿Cómo funciona?").
- **Title** (700, 24px, 1.15): títulos de tarjetas y paneles.
- **Body** (400, 16px): texto corrido, campos de formulario y datos del viaje; 14px en información secundaria.
- **Label** (600, 14px, mayúsculas, espaciado 1px): etiquetas de formulario y antetítulos tipo "PASOS SIMPLES".

### Named Rules
**The Syne Para Titulares Rule.** Syne solo en titulares (h1-h5); nunca en botones, campos, precios ni texto largo, que van en DM Sans.

## Layout

Contenedor centrado de 1280px como máximo, con 16px de margen lateral. La cabecera es fija (72px más su borde) y el contenido vive en un área con scroll propio bajo ella. Los listados de viajes usan rejillas de tarjetas que pasan a una columna en móvil. El ritmo de espaciado sigue múltiplos de 4 y 8px (4, 8, 16, 24, 32).

Puntos de corte en uso: 384px (móvil pequeño), 768px (móvil/tablet) y sus variantes con margen extra (512px, 896px, 1024px). En móvil los botones principales pasan a ancho completo y se apilan.

## Elevation & Depth

Profundidad por capas tonales: cada nivel es un gris algo más claro sobre la noche (`noche` → `noche-2` → `asfalto` → `asfalto-claro`), con bordes de `linea` casi invisibles. En reposo todo es plano; las sombras aparecen como respuesta a la interacción o en elementos flotantes.

### Shadow Vocabulary
- **Sombra flotante** (`box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4)`): menús y paneles que flotan sobre el contenido.
- **Brillo rojo de hover** (`box-shadow: 0 6px 20px rgba(230, 57, 70, 0.35)`): botones rojos al pasar el ratón.
- **Halo de tarjeta** (`box-shadow: 0 8px 32px rgba(230, 57, 70, 0.08)`): tarjetas en hover.
- **Anillo de foco** (`box-shadow: 0 0 0 3px rgba(230, 57, 70, 0.15)`): campos de formulario enfocados.

### Named Rules
**The Plano En Reposo Rule.** Nada lleva sombra en reposo salvo lo que flota (menús, modales, cabecera con desenfoque). La sombra y el brillo son la respuesta a "esto se puede tocar".

## Shapes

Formas amables y redondeadas. Botones y tarjetas a 16px, campos de formulario a 8px, paneles grandes a 24px y chips, etiquetas y contadores en píldora (100px). Los avatares son círculos con borde de 3px en Rojo Piloto. No hay esquinas rectas ni recortes angulosos.

## Components

### Buttons
Redondeados, con peso y que responden al tacto.
- **Shape:** bordes de 16px.
- **Primary:** fondo Rojo Piloto, texto blanco, DM Sans 600 a 0.9rem con un ligero espaciado (0.02em), relleno de 0.7rem × 1.5rem.
- **Hover / Focus:** pasa a Rojo Piloto Hondo, sube 1px y gana el brillo rojo; transición de 0.25s con curva `cubic-bezier(0.4, 0, 0.2, 1)`.
- **Ghost:** transparente, borde de 1.5px en `linea`; en hover el borde y el texto se vuelven rojos sobre un tinte rojo muy suave.
- **Accent:** fondo Ámbar Intermitente con texto oscuro en 700; en hover, ámbar hondo y brillo ámbar.
- **Danger:** rojo sólido para confirmar acciones destructivas, o versión con borde rojo para la acción secundaria.

### Chips
- **Style:** píldora con fondo del color al 15% y texto del color pleno (éxito en verde, aviso en ámbar, peligro en rojo, neutro en gris), con borde del mismo color al 20%.
- **Uso:** plazas libres, "Periódico", "Hacia campus", estados de solicitudes.

### Cards / Containers
- **Corner Style:** 16px.
- **Background:** Asfalto, con borde de `linea`.
- **Shadow Strategy:** plana en reposo; en hover el borde se tiñe de rojo, sube 2px y aparece el halo (ver Elevation & Depth).
- **Internal Padding:** 1.5rem.

### Inputs / Fields
- **Style:** fondo Asfalto, borde de `linea`, radio de 8px, DM Sans 16px, relleno de 8px × 16px.
- **Focus:** borde Rojo Piloto, fondo Asfalto Claro y anillo de foco rojo de 3px.
- **Error:** mensaje en rojo a 0.8rem bajo el campo; los campos obligatorios marcan un asterisco rojo en el label.

### Navigation
- **Cabecera:** fija, con fondo translúcido desenfocado (blur de 16px) y borde inferior de `linea`. Logo a la izquierda, enlaces en el centro (la página activa como píldora sobre Asfalto) y acciones a la derecha (Acceder fantasma, Únete en rojo).
- **Pestañas:** texto DM Sans 600 en gris; la activa en Rojo Piloto con subrayado de 2px.

### Tarjeta de viaje
La pieza más característica de la app. Una franja superior con el sentido del trayecto ("Hacia campus", tintada en verde), la etiqueta "Periódico" y el chip de plazas libres (verde, o rojo si quedan pocas o ninguna); después el trayecto de origen a campus con sus puntos de color, la fecha, la hora y el coche; y al pie el precio grande en ámbar con las acciones "Perfil" y "Ver viaje".

## Do's and Don'ts

### Do:
- **Do** usar solo los colores del frontmatter a través de sus variables (`--primary`, `--accent`, `--surface`...); no introducir hex nuevos.
- **Do** dar a cada elemento pulsable una respuesta visible en hover y foco: subida de 1-2px, cambio de tono y brillo suave, en 0.25s.
- **Do** mantener una sola acción principal en Rojo Piloto por vista (The Una Luz Roja Rule).
- **Do** mostrar siempre a la vista los datos que deciden subirse a un coche: origen, campus, fecha, hora, precio y plazas.
- **Do** desactivar animaciones decorativas con `prefers-reduced-motion`, como ya hace el fondo de la portada.
- **Do** diseñar primero a 375px de ancho y comprobar el contraste AA del texto gris sobre Asfalto.

### Don't:
- **Don't** introducir superficies claras ni un tema claro: UniCar es solo oscuro.
- **Don't** usar Syne en botones, campos, precios o párrafos.
- **Don't** usar esquinas rectas: el mínimo son 8px, y las etiquetas siempre en píldora.
- **Don't** usar el Ámbar Intermitente para acciones destructivas ni el verde para algo que no sea éxito o plazas libres.
- **Don't** usar `texto-3` (#666666) para información que haya que leer; solo para placeholders y detalles prescindibles.
