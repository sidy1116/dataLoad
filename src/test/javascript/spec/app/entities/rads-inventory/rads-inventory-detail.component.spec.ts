import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DataLoadTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RadsInventoryDetailComponent } from '../../../../../../main/webapp/app/entities/rads-inventory/rads-inventory-detail.component';
import { RadsInventoryService } from '../../../../../../main/webapp/app/entities/rads-inventory/rads-inventory.service';
import { RadsInventory } from '../../../../../../main/webapp/app/entities/rads-inventory/rads-inventory.model';

describe('Component Tests', () => {

    describe('RadsInventory Management Detail Component', () => {
        let comp: RadsInventoryDetailComponent;
        let fixture: ComponentFixture<RadsInventoryDetailComponent>;
        let service: RadsInventoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataLoadTestModule],
                declarations: [RadsInventoryDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RadsInventoryService,
                    EventManager
                ]
            }).overrideTemplate(RadsInventoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RadsInventoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RadsInventoryService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RadsInventory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.radsInventory).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
