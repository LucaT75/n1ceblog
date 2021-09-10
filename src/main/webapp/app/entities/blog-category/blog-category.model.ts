export interface IBlogCategory {
  id?: string;
  title?: string | null;
  articles?: string | null;
  artcilesPerRow?: number | null;
}

export class BlogCategory implements IBlogCategory {
  constructor(public id?: string, public title?: string | null, public articles?: string | null, public artcilesPerRow?: number | null) {}
}

export function getBlogCategoryIdentifier(blogCategory: IBlogCategory): string | undefined {
  return blogCategory.id;
}
