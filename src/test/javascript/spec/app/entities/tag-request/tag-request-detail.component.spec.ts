import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DataLoadTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TagRequestDetailComponent } from '../../../../../../main/webapp/app/entities/tag-request/tag-request-detail.component';
import { TagRequestService } from '../../../../../../main/webapp/app/entities/tag-request/tag-request.service';
import { TagRequest } from '../../../../../../main/webapp/app/entities/tag-request/tag-request.model';

describe('Component Tests', () => {

    describe('TagRequest Management Detail Component', () => {
        let comp: TagRequestDetailComponent;
        let fixture: ComponentFixture<TagRequestDetailComponent>;
        let service: TagRequestService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataLoadTestModule],
                declarations: [TagRequestDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TagRequestService,
                    EventManager
                ]
            }).overrideTemplate(TagRequestDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TagRequestDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TagRequestService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TagRequest(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tagRequest).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
