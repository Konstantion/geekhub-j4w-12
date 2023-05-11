export interface HallDto {
  id?: string;
  name?: string;
  createdAt?: Date;
  active?: boolean;

  equals(other: HallDto): boolean;
}