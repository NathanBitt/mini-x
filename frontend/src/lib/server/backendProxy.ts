import { BACKEND_URL } from '$env/static/private';

type ForwardArgs = {
	method: string;
	backendPath: string;
	authHeader?: string | null;
	jsonBody?: unknown;
};

export async function forwardToBackend({ method, backendPath, authHeader, jsonBody }: ForwardArgs) {
	const headers = new Headers();
	if (authHeader) headers.set('Authorization', authHeader);

	if (jsonBody !== undefined) headers.set('Content-Type', 'application/json');

	const res = await fetch(`${BACKEND_URL}${backendPath}`, {
		method,
		headers,
		body: jsonBody !== undefined ? JSON.stringify(jsonBody) : undefined
	});

	// Preserve JSON content-type when present, and properly handle 204/no-body responses.
	const contentType = res.headers.get('content-type') ?? undefined;
	const text = await res.text();

	if (!text) {
		const passthroughHeaders = contentType ? { 'content-type': contentType } : undefined;
		return new Response(null, { status: res.status, headers: passthroughHeaders });
	}

	const passthroughHeaders = contentType ? { 'content-type': contentType } : undefined;
	return new Response(text, { status: res.status, headers: passthroughHeaders });
}

