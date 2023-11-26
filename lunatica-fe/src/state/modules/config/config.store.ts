import { defineStore } from 'pinia'
import { ConfigState, Info } from './config.types'
import { Complaint, User } from './../../../types/common.types'

export const useConfigStore = defineStore('ConfigStore', {
	state: (): ConfigState => ({
		user: {
			id: '',
			roles: [],
			email: ''
		},
		cookie: '',
		info: {
			complaintStates: [],
			countries: [],
			complaintTypes: []
		},
		complaints: []
	}),
	actions: {
		setUser(user: User) {
			this.user = user
		},
		setCookie(cookie: string) {
			this.cookie = cookie
		},
		setInfo(info: Info) {
			this.info = info
		},
		setComplaints(complaints: Complaint[]) {
			this.complaints = complaints
		},
	},
	persist: true
})
