import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { ReTagProfile } from './re-tag-profile.model';
import { ReTagProfilePopupService } from './re-tag-profile-popup.service';
import { ReTagProfileService } from './re-tag-profile.service';

@Component({
    selector: 'jhi-re-tag-profile-delete-dialog',
    templateUrl: './re-tag-profile-delete-dialog.component.html'
})
export class ReTagProfileDeleteDialogComponent {

    reTagProfile: ReTagProfile;

    constructor(
        private reTagProfileService: ReTagProfileService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reTagProfileService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reTagProfileListModification',
                content: 'Deleted an reTagProfile'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Re Tag Profile is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-re-tag-profile-delete-popup',
    template: ''
})
export class ReTagProfileDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reTagProfilePopupService: ReTagProfilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.reTagProfilePopupService
                .open(ReTagProfileDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
