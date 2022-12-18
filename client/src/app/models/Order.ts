export interface Order {
    id: number;
    startPoint: string;
    endPoint: string;
    price?: number;
    startDate: string;
    endDate?: string;
    driverLastName?: string;
    driverFirstName?: string;
    driverRating?: number;
    driverUsername?: string;
    status: string;
}