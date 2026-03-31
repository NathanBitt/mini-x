import { forwardToBackend } from '$lib/server/backendProxy';
import type { RequestHandler } from './$types';

export const DELETE: RequestHandler = async ({ request, params }) => {
	const authHeader = request.headers.get('authorization');
	if (!authHeader) {
		return new Response(JSON.stringify({ message: 'Missing Authorization header' }), {
			status: 401,
			headers: { 'content-type': 'application/json' }
		});
	}

	const id = params.id;
	return forwardToBackend({
		method: 'DELETE',
		backendPath: `/tweets/delete/${encodeURIComponent(id)}`,
		authHeader
	});
};

