import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DetalleRutinaComponent } from './detalle-rutina/detalle-rutina.component';
import { FormularioRutinaComponent } from './formulario-rutina/formulario-rutina.component';
import { FormularioEjercicioComponent } from './formulario-ejercicio/formulario-ejercicio.component';
import { DetalleEjercicioComponent } from './detalle-ejercicio/detalle-ejercicio.component';

@NgModule({
  declarations: [
    AppComponent,
    DetalleRutinaComponent,
    FormularioRutinaComponent,
    FormularioEjercicioComponent,
    DetalleEjercicioComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
