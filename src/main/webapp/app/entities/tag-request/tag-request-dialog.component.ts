import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { TagRequest } from './tag-request.model';
import { TagRequestPopupService } from './tag-request-popup.service';
import { TagRequestService } from './tag-request.service';

@Component({
    selector: 'jhi-tag-request-dialog',
    templateUrl: './tag-request-dialog.component.html'
})
export class TagRequestDialogComponent implements OnInit {

    tagRequest: TagRequest;
    authorities: any[];
    isSaving: boolean;
    createDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private tagRequestService: TagRequestService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, tagRequest, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                tagRequest[field] = base64Data;
                tagRequest[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.tagRequest.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tagRequestService.update(this.tagRequest), false);
        } else {
            this.subscribeToSaveResponse(
                this.tagRequestService.create(this.tagRequest), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TagRequest>, isCreated: boolean) {
        result.subscribe((res: TagRequest) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TagRequest, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Tag Request is created with identifier ${result.id}`
            : `A Tag Request is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'tagRequestListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-tag-request-popup',
    template: ''
})
export class TagRequestPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tagRequestPopupService: TagRequestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.tagRequestPopupService
                    .open(TagRequestDialogComponent, params['id']);
            } else {
                this.modalRef = this.tagRequestPopupService
                    .open(TagRequestDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
