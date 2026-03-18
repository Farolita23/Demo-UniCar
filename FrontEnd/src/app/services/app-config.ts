import { Location } from '@angular/common';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AppConfig {
    
    constructor(
        private location: Location
    ) { }
    
    
    /* ======================= */
    /*   Informacion general   */
    /* ======================= */
    readonly appName = "UniCar";
    readonly disclaimer = `${this.appName} © ${new Date().getFullYear()}`;


    /* ================== */
    /*   Redes sociales   */
    /* ================== */
    readonly socialLinks = {
        "instagram": { label: "Instagram", icon: "/assets/media/icons/sm-instagram.png", route: "/instagram" },
        "facebook" : { label: "Facebook" , icon: "/assets/media/icons/sm-facebook.png" , route: "/facebook"  },
        "x"        : { label: "X"        , icon: "/assets/media/icons/sm-x.png"        , route: "/x"         },
    }


    /* ========= */
    /*   Rutas   */
    /* ========= */
    readonly routes = {
        "index"       : { label: 'Inicio'        , route: '/'            , refer: false, icon: '/assets/media/icons/ui-home.png'     },
        "search_trip" : { label: 'Viajes'        , route: '/search-trip' , refer: false, icon: '/assets/media/icons/ui-home.png'     },
        "login"       : { label: 'Acceder'       , route: '/login'       , refer: true , icon: '/assets/media/icons/ui-profile.png'  },
        "signup"      : { label: 'Unirse'        , route: '/signup'      , refer: true , icon: '/assets/media/icons/ui-profile.png'  },
        "profile"     : { label: 'Perfil'        , route: '/profile/me'  , refer: false, icon: '/assets/media/icons/ui-profile.png'  },
        "logout"      : { label: 'Salir'         , route: '/logout'      , refer: false, icon: '/assets/media/icons/ui-profile.png'  },
        "faq"         : { label: 'FAQ'           , route: '/faq'         , refer: false, icon: '/assets/media/icons/ui-profile.png'  },
        "about"       : { label: 'Sobre nosotros', route: '/about'       , refer: false, icon: '/assets/media/icons/ui-settings.png' },
    }

    /* ========== */
    /*   Header   */
    /* ========== */
    readonly header = [
        { logged: null , link: this.routes.index       },
        { logged: null , link: this.routes.search_trip },
        { logged: false, link: this.routes.login       },
        { logged: false, link: this.routes.signup      },
        { logged: true , link: this.routes.profile     },
        { logged: true , link: this.routes.logout      },
    ]
    readonly footer = {
        group2: this.header,
        group3: [
            { logged: null, link: this.routes.faq },
            { logged: null, link: this.routes.about },
        ],
        group4: [
            this.socialLinks.facebook,
            this.socialLinks.x,
            this.socialLinks.instagram,
        ]
    }

    /* ============== */
    /*   Utilidades   */
    /* ============== */
    refer(): string | null {
        const params = new URLSearchParams(this.location.path(true).split('?')[1]);
        return params.get('refer') || '/';
    }

}
