import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CityViewerComponent } from './city-viewer.component';

describe('CityViewerComponent', () => {
  let component: CityViewerComponent;
  let fixture: ComponentFixture<CityViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CityViewerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CityViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
