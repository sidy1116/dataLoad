import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DataLoadTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RadsCategoryInventoryDetailComponent } from '../../../../../../main/webapp/app/entities/rads-category-inventory/rads-category-inventory-detail.component';
import { RadsCategoryInventoryService } from '../../../../../../main/webapp/app/entities/rads-category-inventory/rads-category-inventory.service';
import { RadsCategoryInventory } from '../../../../../../main/webapp/app/entities/rads-category-inventory/rads-category-inventory.model';

describe('Component Tests', () => {

    describe('RadsCategoryInventory Management Detail Component', () => {
        let comp: RadsCategoryInventoryDetailComponent;
        let fixture: ComponentFixture<RadsCategoryInventoryDetailComponent>;
        let service: RadsCategoryInventoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataLoadTestModule],
                declarations: [RadsCategoryInventoryDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RadsCategoryInventoryService,
                    EventManager
                ]
            }).overrideTemplate(RadsCategoryInventoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RadsCategoryInventoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RadsCategoryInventoryService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RadsCategoryInventory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.radsCategoryInventory).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
