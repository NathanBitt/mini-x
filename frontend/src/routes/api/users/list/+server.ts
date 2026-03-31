import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const GET: RequestHandler = async ({ request }) => {
	const authHeader = request.headers.get('authorization');
	if (!authHeader) {
		return new Response(JSON.stringify({ message: 'Missing Authorization header' }), {
			status: 401,
			headers: { 'content-type': 'application/json' }
		});
	}

	return forwardToBackend({
		method: 'GET',
		backendPath: '/users/list',
		authHeader
	});
};

