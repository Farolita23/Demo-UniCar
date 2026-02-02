import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CookieService } from 'ngx-cookie-service';
import { App } from '../../app';
import { BrowserModule } from '@angular/platform-browser';
import { finalize, Observable } from 'rxjs';



@NgModule({
    imports: [BrowserModule, App],
    providers: [CookieService],
    bootstrap: []
})
export class AppModule {}
