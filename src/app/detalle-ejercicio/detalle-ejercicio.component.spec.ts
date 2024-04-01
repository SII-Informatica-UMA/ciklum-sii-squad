import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleEjercicioComponent } from './detalle-ejercicio.component';

describe('DetalleEjercicioComponent', () => {
  let component: DetalleEjercicioComponent;
  let fixture: ComponentFixture<DetalleEjercicioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalleEjercicioComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleEjercicioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
