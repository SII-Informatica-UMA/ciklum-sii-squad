import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormularioEjercicioComponent } from './formulario-ejercicio.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

describe('FormularioEjercicioComponent', () => {
  let component: FormularioEjercicioComponent;
  let fixture: ComponentFixture<FormularioEjercicioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormularioEjercicioComponent ],
      imports: [FormsModule],
      providers: [NgbActiveModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioEjercicioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
