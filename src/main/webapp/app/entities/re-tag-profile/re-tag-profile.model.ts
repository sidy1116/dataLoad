export class ReTagProfile {
    constructor(
        public id?: number,
        public siteId?: number,
        public inputFile?: any,
        public phint?: string,
        public headers?: string,
        public createDate?: any,
        public startFromLine?: number,
        public toLine?: number,
    ) {
    }
}
