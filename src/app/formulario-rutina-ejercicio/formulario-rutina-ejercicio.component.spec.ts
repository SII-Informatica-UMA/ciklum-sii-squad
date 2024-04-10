import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioRutinaEjercicioComponent } from './formulario-rutina-ejercicio.component';

describe('FormularioRutinaEjercicioComponent', () => {
  let component: FormularioRutinaEjercicioComponent;
  let fixture: ComponentFixture<FormularioRutinaEjercicioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormularioRutinaEjercicioComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioRutinaEjercicioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
