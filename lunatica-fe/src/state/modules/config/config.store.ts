import { defineStore } from 'pinia'
import { ConfigState, User } from './config.types'

export const useConfigStore = defineStore('ConfigStore', {
	state: (): ConfigState => ({
		user: {
			id: '',
			roles: [],
			email: ''
		},
		cookie: ''
	}),
	actions: {
		setUser(user: User) {
			this.user = user
		},
		setCookie(cookie: string) {
			this.cookie = cookie
		}
	},
	persist: true
})
