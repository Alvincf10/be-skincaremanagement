import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IngridientDetailComponent } from './ingridient-detail.component';

describe('Ingridient Management Detail Component', () => {
  let comp: IngridientDetailComponent;
  let fixture: ComponentFixture<IngridientDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IngridientDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ingridient: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IngridientDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IngridientDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ingridient on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ingridient).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
