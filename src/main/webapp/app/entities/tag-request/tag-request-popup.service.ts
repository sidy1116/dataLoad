import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TagRequest } from './tag-request.model';
import { TagRequestService } from './tag-request.service';

@Injectable()
export class TagRequestPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tagRequestService: TagRequestService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.tagRequestService.find(id).subscribe((tagRequest) => {
                if (tagRequest.createDate) {
                    tagRequest.createDate = {
                        year: tagRequest.createDate.getFullYear(),
                        month: tagRequest.createDate.getMonth() + 1,
                        day: tagRequest.createDate.getDate()
                    };
                }
                this.tagRequestModalRef(component, tagRequest);
            });
        } else {
            return this.tagRequestModalRef(component, new TagRequest());
        }
    }

    tagRequestModalRef(component: Component, tagRequest: TagRequest): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tagRequest = tagRequest;
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
