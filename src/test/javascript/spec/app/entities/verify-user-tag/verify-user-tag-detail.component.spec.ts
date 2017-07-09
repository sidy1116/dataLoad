import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { DataLoadTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VerifyUserTagDetailComponent } from '../../../../../../main/webapp/app/entities/verify-user-tag/verify-user-tag-detail.component';
import { VerifyUserTagService } from '../../../../../../main/webapp/app/entities/verify-user-tag/verify-user-tag.service';
import { VerifyUserTag } from '../../../../../../main/webapp/app/entities/verify-user-tag/verify-user-tag.model';

describe('Component Tests', () => {

    describe('VerifyUserTag Management Detail Component', () => {
        let comp: VerifyUserTagDetailComponent;
        let fixture: ComponentFixture<VerifyUserTagDetailComponent>;
        let service: VerifyUserTagService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataLoadTestModule],
                declarations: [VerifyUserTagDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VerifyUserTagService,
                    EventManager
                ]
            }).overrideTemplate(VerifyUserTagDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VerifyUserTagDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VerifyUserTagService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new VerifyUserTag(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.verifyUserTag).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
