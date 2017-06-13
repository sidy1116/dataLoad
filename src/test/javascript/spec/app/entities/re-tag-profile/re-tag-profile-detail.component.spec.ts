import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DataLoadTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReTagProfileDetailComponent } from '../../../../../../main/webapp/app/entities/re-tag-profile/re-tag-profile-detail.component';
import { ReTagProfileService } from '../../../../../../main/webapp/app/entities/re-tag-profile/re-tag-profile.service';
import { ReTagProfile } from '../../../../../../main/webapp/app/entities/re-tag-profile/re-tag-profile.model';

describe('Component Tests', () => {

    describe('ReTagProfile Management Detail Component', () => {
        let comp: ReTagProfileDetailComponent;
        let fixture: ComponentFixture<ReTagProfileDetailComponent>;
        let service: ReTagProfileService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataLoadTestModule],
                declarations: [ReTagProfileDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReTagProfileService,
                    EventManager
                ]
            }).overrideTemplate(ReTagProfileDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReTagProfileDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReTagProfileService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ReTagProfile(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.reTagProfile).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
