
const enum IdType {
    'bkuuid',
    'adid',
    'idfa',
    'e_id_m',
    'e_id_s',
    'p_id_m',
    'p_id_s'

};

const enum Status {
    'ACTIVE',
    'FAIL',
    'SUCCESS'

};
export class TagRequest {
    constructor(
        public id?: number,
        public siteId?: number,
        public phints?: string,
        public referelUrl?: string,
        public headers?: string,
        public idType?: IdType,
        public requestCount?: number,
        public file?: any,
        public createDate?: any,
        public status?: Status,
    ) {
    }
}
