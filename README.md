This is an example how spring data rest could work with "rich" jsons: when we want to save 
parents altogether with childs.
In summary: 
* use cascade type when you want to do it.
* don't create repository for entities which you're going to fetch only
using rich json. Or create but use `@RepositoryRestResource(exported = false)`
