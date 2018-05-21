# Vemmio tunnel pool

This is piece of code which should be responsible for managing tunnels.<br>
Tunnel is a connection between device and server, each tunnel has unique id.<br>
TunnelPool is responsible for caching, opening and closing tunnels.

TunnelPool requirements:
<ul>
<li> It should be only one opened Tunnel with specific TunnelId inside the pool.
<li> Tunnel should be reused when is already opened
<li> There could be error when trying open the Tunnel
<li> Pool should be thread safe
<li> Getting Tunnel from the pool should be non blocking
<li> All opened tunnels should be closed when pool is closed (shutdown)
</ul>

## Components
<ul>
<li>App - main program
<li>Tunnel - tunnel betwen device and server
<li>TunnelId - id of the tunnel
<li>TunnelFactory - creates tunnel with specific id
<li>TunnelPool - thread safe, non blocking pool of tunnels
</ul>

## Building and running

mvn clean install

## Prerequisites

<ul>
<li>Java 8
<li>Maven 3
</ul>

  
 
