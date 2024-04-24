import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleRutinaComponent } from './detalle-rutina.component';

describe('DetalleRutinaComponent', () => {
  let component: DetalleRutinaComponent;
  let fixture: ComponentFixture<DetalleRutinaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalleRutinaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleRutinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
