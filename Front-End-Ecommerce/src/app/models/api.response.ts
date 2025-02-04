export interface ApiResponse<T = any> {
    status: boolean;
    message?: string;
    data?: T;
}