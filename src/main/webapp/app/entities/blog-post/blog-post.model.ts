export interface IBlogPost {
  id?: string;
  title?: string | null;
  content?: string | null;
  snippet?: string | null;
  expertId?: string | null;
  featuredImg?: string | null;
  category?: string | null;
  publishingTime?: string | null;
}

export class BlogPost implements IBlogPost {
  constructor(
    public id?: string,
    public title?: string | null,
    public content?: string | null,
    public snippet?: string | null,
    public expertId?: string | null,
    public featuredImg?: string | null,
    public category?: string | null,
    public publishingTime?: string | null
  ) {}
}

export function getBlogPostIdentifier(blogPost: IBlogPost): string | undefined {
  return blogPost.id;
}
