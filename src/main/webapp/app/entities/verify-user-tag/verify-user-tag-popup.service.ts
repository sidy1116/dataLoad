import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { VerifyUserTag } from './verify-user-tag.model';
import { VerifyUserTagService } from './verify-user-tag.service';

@Injectable()
export class VerifyUserTagPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private verifyUserTagService: VerifyUserTagService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.verifyUserTagService.find(id).subscribe((verifyUserTag) => {
                if (verifyUserTag.verifyDate) {
                    verifyUserTag.verifyDate = {
                        year: verifyUserTag.verifyDate.getFullYear(),
                        month: verifyUserTag.verifyDate.getMonth() + 1,
                        day: verifyUserTag.verifyDate.getDate()
                    };
                }
                this.verifyUserTagModalRef(component, verifyUserTag);
            });
        } else {
            return this.verifyUserTagModalRef(component, new VerifyUserTag());
        }
    }

    verifyUserTagModalRef(component: Component, verifyUserTag: VerifyUserTag): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.verifyUserTag = verifyUserTag;
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
