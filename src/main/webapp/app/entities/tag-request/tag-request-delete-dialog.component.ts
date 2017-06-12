import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { TagRequest } from './tag-request.model';
import { TagRequestPopupService } from './tag-request-popup.service';
import { TagRequestService } from './tag-request.service';

@Component({
    selector: 'jhi-tag-request-delete-dialog',
    templateUrl: './tag-request-delete-dialog.component.html'
})
export class TagRequestDeleteDialogComponent {

    tagRequest: TagRequest;

    constructor(
        private tagRequestService: TagRequestService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tagRequestService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tagRequestListModification',
                content: 'Deleted an tagRequest'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Tag Request is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-tag-request-delete-popup',
    template: ''
})
export class TagRequestDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tagRequestPopupService: TagRequestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.tagRequestPopupService
                .open(TagRequestDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
