import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { VerifyUserTag } from './verify-user-tag.model';
import { VerifyUserTagPopupService } from './verify-user-tag-popup.service';
import { VerifyUserTagService } from './verify-user-tag.service';

@Component({
    selector: 'jhi-verify-user-tag-delete-dialog',
    templateUrl: './verify-user-tag-delete-dialog.component.html'
})
export class VerifyUserTagDeleteDialogComponent {

    verifyUserTag: VerifyUserTag;

    constructor(
        private verifyUserTagService: VerifyUserTagService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.verifyUserTagService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'verifyUserTagListModification',
                content: 'Deleted an verifyUserTag'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Verify User Tag is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-verify-user-tag-delete-popup',
    template: ''
})
export class VerifyUserTagDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verifyUserTagPopupService: VerifyUserTagPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.verifyUserTagPopupService
                .open(VerifyUserTagDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
