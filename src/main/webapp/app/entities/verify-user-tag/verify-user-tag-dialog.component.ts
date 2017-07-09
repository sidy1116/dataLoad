import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { VerifyUserTag } from './verify-user-tag.model';
import { VerifyUserTagPopupService } from './verify-user-tag-popup.service';
import { VerifyUserTagService } from './verify-user-tag.service';

@Component({
    selector: 'jhi-verify-user-tag-dialog',
    templateUrl: './verify-user-tag-dialog.component.html'
})
export class VerifyUserTagDialogComponent implements OnInit {

    verifyUserTag: VerifyUserTag;
    authorities: any[];
    isSaving: boolean;
    verifyDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private verifyUserTagService: VerifyUserTagService,
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

    setFileData(event, verifyUserTag, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                verifyUserTag[field] = base64Data;
                verifyUserTag[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.verifyUserTag.id !== undefined) {
            this.subscribeToSaveResponse(
                this.verifyUserTagService.update(this.verifyUserTag), false);
        } else {
            this.subscribeToSaveResponse(
                this.verifyUserTagService.create(this.verifyUserTag), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<VerifyUserTag>, isCreated: boolean) {
        result.subscribe((res: VerifyUserTag) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: VerifyUserTag, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Verify User Tag is created with identifier ${result.id}`
            : `A Verify User Tag is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'verifyUserTagListModification', content: 'OK'});
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
    selector: 'jhi-verify-user-tag-popup',
    template: ''
})
export class VerifyUserTagPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verifyUserTagPopupService: VerifyUserTagPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.verifyUserTagPopupService
                    .open(VerifyUserTagDialogComponent, params['id']);
            } else {
                this.modalRef = this.verifyUserTagPopupService
                    .open(VerifyUserTagDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
