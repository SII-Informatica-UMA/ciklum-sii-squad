import { TestBed } from '@angular/core/testing';

import { RutinasService } from './rutina.service';

describe('RutinasService', () => {
  let service: RutinasService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RutinasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
