import { Author } from "./author.model";

export interface Book {
  id: number;
  title: string;
  subtitle?: string;
  isbn: string;
  description?: string;
  publicationYear: number;
  authors: Author[];
  genres: Genre[];
}


export interface Genre {
  id: number;
  name: string;
  description?: string;
  intensity?: string;
}
