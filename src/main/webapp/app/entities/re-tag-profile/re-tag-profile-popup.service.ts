import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ReTagProfile } from './re-tag-profile.model';
import { ReTagProfileService } from './re-tag-profile.service';

@Injectable()
export class ReTagProfilePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private reTagProfileService: ReTagProfileService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.reTagProfileService.find(id).subscribe((reTagProfile) => {
                if (reTagProfile.createDate) {
                    reTagProfile.createDate = {
                        year: reTagProfile.createDate.getFullYear(),
                        month: reTagProfile.createDate.getMonth() + 1,
                        day: reTagProfile.createDate.getDate()
                    };
                }
                this.reTagProfileModalRef(component, reTagProfile);
            });
        } else {
            return this.reTagProfileModalRef(component, new ReTagProfile());
        }
    }

    reTagProfileModalRef(component: Component, reTagProfile: ReTagProfile): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.reTagProfile = reTagProfile;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
