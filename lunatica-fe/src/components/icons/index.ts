const context = import.meta.glob('./*.svg', { eager: true, import: 'default' })
const modules: { [key: string]: any } = {}

for (const [path, component] of Object.entries(context)) {
	// in path we get path to component f.e. ./DialogueActivity.vue
	// and we need to remove ./ and .vue parts
	const name = path.match(/\.\/(.*)\.svg/)?.at(1) || ''
	modules[name] = component
}

export const icons = modules
